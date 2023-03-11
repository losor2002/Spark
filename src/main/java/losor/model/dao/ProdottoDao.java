package losor.model.dao;

import losor.model.bean.Prodotto;

public class ProdottoDao extends AbstractDao<Prodotto>
{
    private static final ProdottoDao instance = new ProdottoDao();

    private ProdottoDao()
    {
        super(Prodotto.class);
    }

    public static ProdottoDao getInstance()
    {
        return instance;
    }
}