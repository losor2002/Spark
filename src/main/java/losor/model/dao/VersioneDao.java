package losor.model.dao;

import losor.model.bean.Versione;

public class VersioneDao extends AbstractDao<Versione>
{
    private static final VersioneDao instance = new VersioneDao();

    private VersioneDao()
    {
        super(Versione.class);
    }

    public static VersioneDao getInstance()
    {
        return instance;
    }
}