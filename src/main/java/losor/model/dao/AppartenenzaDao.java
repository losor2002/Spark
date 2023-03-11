package losor.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import losor.model.bean.Appartenenza;
import losor.model.bean.Categoria;
import losor.model.bean.Prodotto;
import losor.model.util.DbConnection;

public class AppartenenzaDao extends AbstractDao<Appartenenza>
{
    private static final AppartenenzaDao instance = new AppartenenzaDao();

    private AppartenenzaDao()
    {
        super(Appartenenza.class);
    }

    public static AppartenenzaDao getInstance()
    {
        return instance;
    }

    public static Map<Categoria, List<Prodotto>> getProdottiByCategoria() throws SQLException
    {
        try(Connection connection = DbConnection.getConnection())
        {
            try(Statement statement = connection.createStatement())
            {
                String query = "SELECT categoria.numProgressivo, categoria.tipo, categoria.sottotipo, prodotto.numProgressivo, prodotto.nome, prodotto.icona, prodotto.cancellato, prodotto.produttore\n"
                            +  "FROM appartenenza join categoria on categoria = numProgressivo join prodotto on prodotto = prodotto.numProgressivo\n"
                            +  "ORDER BY categoria.numProgressivo";

                ResultSet res = statement.executeQuery(query);
                if(!res.next())
                    return Map.of();

                Map<Categoria, List<Prodotto>> map = new HashMap<>();
                List<Prodotto> prodotti = null;
                Categoria categoria = null;

                do
                {
                    if(categoria == null || (categoria.getNumProgressivo() != res.getInt("categoria.numProgressivo")))
                    {
                        categoria = new Categoria(res.getInt("categoria.numProgressivo"), res.getString("categoria.tipo"), res.getString("categoria.sottotipo"));
                        prodotti = new ArrayList<>();
                        map.put(categoria, prodotti);
                    }

                    Prodotto prodotto = new Prodotto();
                    prodotto.setNumProgressivo(res.getInt("prodotto.numProgressivo"));
                    prodotto.setNome(res.getString("prodotto.nome"));
                    prodotto.setIcona(res.getString("prodotto.icona"));
                    prodotto.setCancellato(res.getBoolean("prodotto.cancellato"));
                    prodotto.setProduttore(res.getString("prodotto.produttore"));
                    prodotti.add(prodotto);
                }while(res.next());

                return map;
            }
        }
    }
}