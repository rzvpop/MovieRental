package Domain;

public class Client extends BaseEntity<Long>
{
    private String name;
    private int age;

    public Client(String name, int age)
    {
        this.name = name;
        this.age = age;
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
        return name + " | age: " + Integer.toString(age);
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
}
