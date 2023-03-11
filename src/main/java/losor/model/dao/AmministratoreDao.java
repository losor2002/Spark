package losor.model.dao;

import losor.model.bean.Amministratore;

public class AmministratoreDao extends AbstractDao<Amministratore>
{
    private static final AmministratoreDao instance = new AmministratoreDao();

    private AmministratoreDao()
    {
        super(Amministratore.class);
    }

    public static AmministratoreDao getInstance()
    {
        return instance;
    }
}