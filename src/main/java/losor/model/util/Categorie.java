package losor.model.util;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import losor.model.bean.Categoria;
import losor.model.dao.CategoriaDao;
import losor.model.dao.Dao;

public class Categorie
{
    private static Map<String, List<Categoria>> instance = null;

    private Categorie() {}

    private static Map<String, List<Categoria>> getCategorieByTipo()
    {
        Dao<Categoria> categoriaDao = CategoriaDao.getInstance();
        List<Categoria> categorie = null;

        try
        {
            categorie = categoriaDao.getAll(null);
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Impossibile caricare le categorie dal database");
        }

        Map<String, List<Categoria>> categorieMap = categorie.stream()
                                                .sorted(Comparator.comparing(Categoria::getSottotipo))
                                                .collect(Collectors.groupingBy(Categoria::getTipo));

        return categorieMap;
    }

    public static synchronized Map<String, List<Categoria>> getInstance()
    {
        if(instance == null)
            instance = getCategorieByTipo();
        
        return instance;
    }

    public static synchronized void invalidate()
    {
        instance = null;
    }
}
