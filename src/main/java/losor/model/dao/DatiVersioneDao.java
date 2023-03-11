package losor.model.dao;

import losor.model.bean.DatiVersione;

public class DatiVersioneDao extends AbstractDao<DatiVersione>
{
    private static final DatiVersioneDao instance = new DatiVersioneDao();

    private DatiVersioneDao()
    {
        super(DatiVersione.class);
    }

    public static DatiVersioneDao getInstance()
    {
        return instance;
    }
}