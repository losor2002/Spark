package losor.model.dao;

import losor.model.bean.Categoria;

public class CategoriaDao extends AbstractDao<Categoria>
{
    private static final CategoriaDao instance = new CategoriaDao();

    private CategoriaDao()
    {
        super(Categoria.class);
    }

    public static CategoriaDao getInstance()
    {
        return instance;
    }
}