package losor.model.dao;

import losor.model.bean.Immagine;

public class ImmagineDao extends AbstractDao<Immagine>
{
    private static final ImmagineDao instance = new ImmagineDao();

    private ImmagineDao()
    {
        super(Immagine.class);
    }

    public static ImmagineDao getInstance()
    {
        return instance;
    }
}