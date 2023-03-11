package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Prodotto;
import losor.model.dao.ProdottoDao;
import losor.model.util.Categorie;
import losor.model.util.CategorieByProdotto;

@WebServlet(urlPatterns = {"/search"},
            initParams = {@WebInitParam(name = "searchJsp", value = "/jsp/search.jsp")})
public class SearchServlet extends HttpServlet
{
    private String searchJsp;

    @Override
    public void init() throws ServletException
    {
        searchJsp = getInitParameter("searchJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String[] categorieParam = req.getParameterValues("categoria");
        String cerca = req.getParameter("cerca");

        Set<Integer> categorie;
        if(categorieParam == null || ParameterUtil.isEmpty(categorieParam[0]))
        {
            categorie = Set.of();
        }
        else
        {
            categorie = Stream.of(categorieParam).map(Integer::valueOf).collect(Collectors.toSet());
        }

        Predicate<Prodotto> prodottoPredicate;
        if(ParameterUtil.isEmpty(cerca))
        {
            prodottoPredicate = e -> true;
        }
        else
        {
            String[] stringhe = cerca.toLowerCase().split("\\s");
            prodottoPredicate = e -> {
                                        String nome = e.getNome().toLowerCase();
                                        return Stream.of(stringhe).allMatch(s -> nome.contains(s));
                                     };
        }

        List<Prodotto> prodotti;
        try
        {
            prodotti = ProdottoDao.getInstance().getAll(null);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        Map<Integer, Set<Integer>> categorieByProdotto = CategorieByProdotto.getInstance();

        List<Prodotto> prodottiFiltrati = prodotti.parallelStream()
                                         .filter(prodottoPredicate)
                                         .filter(e -> categorieByProdotto.get(e.getNumProgressivo()).containsAll(categorie))
                                         .sorted(Comparator.comparing(Prodotto::getNome))
                                         .toList();

        req.setAttribute("prodotti", prodottiFiltrati);
        req.setAttribute("categorie", Categorie.getInstance());
        req.setAttribute("categorieFiltrate", categorie);
        req.getRequestDispatcher(searchJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
