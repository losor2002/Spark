package losor.model.dao;

import losor.model.bean.Carrello;

public class CarrelloDao extends AbstractDao<Carrello>
{
    private static final CarrelloDao instance = new CarrelloDao();

    private CarrelloDao()
    {
        super(Carrello.class);
    }

    public static CarrelloDao getInstance()
    {
        return instance;
    }
}