package Domain;

import java.util.Arrays;
import java.util.List;

public class Client extends BaseEntity<Long>
{
    private String name;
    private int age;

    public Client(String info){
        List<String> items = Arrays.asList(info.split(","));
        String name_p = items.get(1);
        int age_p = Integer.parseInt(items.get(2));
        this.name = name_p;
        this.age = age_p;
    }

    public Client(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public Client trick(String info)
    {
        return new Client(info);
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String toString()
    {
        return "Id: " + getId().toString() + " | " + name + " | age: " + Integer.toString(age);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Client client = (Client) o;

        return name.equals(client.name);
    }

    public String csv(){
        return getId().toString() + "," + name + "," +  Integer.toString(age);
    }
}
