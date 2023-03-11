package losor.model.dao;

import losor.model.bean.Composizione;

public class ComposizioneDao extends AbstractDao<Composizione>
{
    private static final ComposizioneDao instance = new ComposizioneDao();

    private ComposizioneDao()
    {
        super(Composizione.class);
    }

    public static ComposizioneDao getInstance()
    {
        return instance;
    }
}