package losor.model.dao;

import losor.model.bean.Ordine;

public class OrdineDao extends AbstractDao<Ordine>
{
    private static final OrdineDao instance = new OrdineDao();

    private OrdineDao()
    {
        super(Ordine.class);
    }

    public static OrdineDao getInstance()
    {
        return instance;
    }
}