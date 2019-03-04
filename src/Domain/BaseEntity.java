package Domain;

public class BaseEntity<ID>
{
    private ID id;

    public BaseEntity()
    {
    }

    public ID getId()
    {
        return id;
    }

    public void setId(ID _id)
    {
        id = _id;
    }

    public String toString()
    {
        return id.toString();
    }
}
