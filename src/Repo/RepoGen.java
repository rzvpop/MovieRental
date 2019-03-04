package Repo;

import java.util.ArrayList;
import java.util.Comparator;

public class RepoGen<T>
{
    private ArrayList<T> items;

    public RepoGen()
    {
        items = new ArrayList<>();
    }

    private int contains(T t)
    {
        int sought = -1;
        int left = 0, right = items.size() - 1;

        while(left < right)
        {
            if(((Comparable) t).compareTo(items.get((left + right) / 2)) <= 0)
            {
                right = (left + right) / 2;
            }
            else if(((Comparable) t).compareTo(items.get((left + right) / 2)) > 0)
            {
                left = (left + right) / 2;
            }
        }

        if(((Comparable) t).compareTo(items.get(left)) == 0)
        {
            sought = left;
        }

        return sought;
    }

    public void add(T t)
    {
        int left = 0, right = items.size() - 1;

        if(t instanceof Comparable)
        {
            while(left < right)
            {
                if(((Comparable) t).compareTo(items.get((left + right) / 2)) <= 0)
                {
                    right = (left + right) / 2;
                }
                else if(((Comparable) t).compareTo(items.get((left + right) / 2)) > 0)
                {
                    left = (left + right) / 2;
                }
            }

            if(((Comparable) t).compareTo(items.get(left)) != 0)
            {
                items.add(left, t);
            }
        }
    }

    public void remove(T t)
    {
        int aux = contains(t);

        if(aux != -1)
        {
            items.remove(aux);
        }
    }

    public T get(int i)
    {
        return items.get(i);
    }

    public int getIndex(T t)
    {
        return items.indexOf(t);
    }

    public ArrayList<T> getAll()
    {
        return items;
    }

    public void update(T t)
    {
        int aux = contains(t);

        if(aux != -1)
        {
            items.set(aux, t);
        }
    }

}
