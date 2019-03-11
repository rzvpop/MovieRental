package Repo;

import Domain.BaseEntity;
import Domain.Validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rzv
 */

public class Repo<ID, T extends BaseEntity<ID>> implements IRepo<ID, T>
{
    private Map<ID, T> items;
    private Validator<T> validator;

    public Repo(Validator<T> _validator)
    {
        items = new HashMap<>();
        validator = _validator;
    }

    @Override
    public Optional<T> delete(ID id)
    {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        return Optional.ofNullable(items.remove(id));
    }

    @Override
    public Iterable<T> findAll()
    {
        return items.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    public Optional<T> findOne(ID id)
    {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Optional<T> save(T entity)
    {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(entity);
        return Optional.ofNullable(items.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<T> update(T entity)
    {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(entity);
        return Optional.ofNullable(items.computeIfPresent(entity.getId(), (k,v) -> entity));
    }

    public int size()
    {
        return items.size();
    }
}
