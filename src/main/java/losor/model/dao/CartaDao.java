package losor.model.dao;

import losor.model.bean.Carta;

public class CartaDao extends AbstractDao<Carta>
{
    private static final CartaDao instance = new CartaDao();

    private CartaDao()
    {
        super(Carta.class);
    }

    public static CartaDao getInstance()
    {
        return instance;
    }
}