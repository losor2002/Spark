package losor.model.dao;

import losor.model.bean.Produttore;

public class ProduttoreDao extends AbstractDao<Produttore>
{
    private static final ProduttoreDao instance = new ProduttoreDao();

    private ProduttoreDao()
    {
        super(Produttore.class);
    }

    public static ProduttoreDao getInstance()
    {
        return instance;
    }
}