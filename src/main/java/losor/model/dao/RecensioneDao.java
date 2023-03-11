package losor.model.dao;

import losor.model.bean.Recensione;

public class RecensioneDao extends AbstractDao<Recensione>
{
    private static final RecensioneDao instance = new RecensioneDao();

    private RecensioneDao()
    {
        super(Recensione.class);
    }

    public static RecensioneDao getInstance()
    {
        return instance;
    }
}