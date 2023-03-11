package losor.model.dao;

import losor.model.bean.Indirizzo;

public class IndirizzoDao extends AbstractDao<Indirizzo>
{
    private static final IndirizzoDao instance = new IndirizzoDao();

    private IndirizzoDao()
    {
        super(Indirizzo.class);
    }

    public static IndirizzoDao getInstance()
    {
        return instance;
    }
}