package Repo;

import Domain.BaseEntity;

import java.util.Optional;

public interface IRepo<ID, T extends BaseEntity<ID>>
{
    Optional<T> delete(ID id);
    Iterable<T> findAll();
    Optional<T> findOne(ID id);
    Optional<T> save(T entity);
    Optional<T> update(T entity);
}