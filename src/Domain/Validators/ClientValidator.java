package Domain.Validators;

import Domain.Client;

public class ClientValidator implements Validator<Client>
{
    @Override
    public void validate(Client client) throws ValidatorException
    {
        if(client.getName().equals(""))
            throw new ValidatorException("Name can't be empty string");

        if(client.getAge() < 1)
            throw new ValidatorException("Invalid age");
    }
}
