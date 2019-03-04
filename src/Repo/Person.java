package Repo;

import java.util.Comparator;

public class Person implements Comparable<Person>
{
    int age;
    int x;

    public Person(int _age, int _x)
    {
        age = _age;
        x = _x;
    }

    @Override
    public int compareTo(Person p)
    {
        return age - p.age;
    }

    public int getAge()
    {
        return age;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int _x)
    {
        x = _x;
    }

    public String toString()
    {
        return Integer.toString(age) + ' ' + Integer.toString(x);
    }
}
