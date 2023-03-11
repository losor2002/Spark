package losor.model.util;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import losor.model.bean.Appartenenza;
import losor.model.dao.AppartenenzaDao;

public class CategorieByProdotto
{
    private static Map<Integer, Set<Integer>> instance = null;

    private CategorieByProdotto() {}

    public static synchronized Map<Integer, Set<Integer>> getInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = AppartenenzaDao.getInstance().getAll(null).stream().collect(Collectors.groupingBy(Appartenenza::getProdotto, Collectors.mapping(Appartenenza::getCategoria, Collectors.toSet())));
            }
            catch(SQLException e)
            {
                throw new RuntimeException("impossibile prelevare dal database le categorie raggruppate per prodotto");
            }
        }

        return instance;
    }

    public static synchronized void invalidate()
    {
        instance = null;
    }
}
