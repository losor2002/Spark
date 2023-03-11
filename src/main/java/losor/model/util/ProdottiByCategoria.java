package losor.model.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import losor.model.bean.Categoria;
import losor.model.bean.Prodotto;
import losor.model.dao.AppartenenzaDao;

public class ProdottiByCategoria
{
    private static Map<Categoria, List<Prodotto>> instance = null;

    private ProdottiByCategoria() {}

    public static synchronized Map<Categoria, List<Prodotto>> getInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = AppartenenzaDao.getProdottiByCategoria();
            }
            catch(SQLException e)
            {
                throw new RuntimeException("impossibile prelevare dal database i prodotti raggruppati per categoria");
            }
        }

        return instance;
    }

    public static synchronized void invalidate()
    {
        instance = null;
    }
}
