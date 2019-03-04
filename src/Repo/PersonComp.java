package Repo;

import java.util.Comparator;
import Repo.Person;

public class PersonComp implements Comparator<Person>
{
    @Override
    public int compare(Person p1, Person p2)
    {
        return p1.getAge() - p2.getAge();
    }
}
