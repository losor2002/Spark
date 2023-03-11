package losor.model.dao;

import losor.model.bean.Cliente;

public class ClienteDao extends AbstractDao<Cliente>
{
    private static final ClienteDao instance = new ClienteDao();

    private ClienteDao()
    {
        super(Cliente.class);
    }

    public static ClienteDao getInstance()
    {
        return instance;
    }
}