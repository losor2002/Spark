package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.bean.Prodotto;
import losor.model.dao.Dao;
import losor.model.dao.ProdottoDao;
import losor.model.util.VersioneAggregata;

@WebServlet(urlPatterns = {"/productDetails"},
            initParams = {@WebInitParam(name = "productDetailsJsp", value = "/jsp/productDetails.jsp")})
public class ProductDetailsServlet extends HttpServlet
{
    private String productDetailsJsp;

    @Override
    public void init() throws ServletException
    {
        productDetailsJsp = getInitParameter("productDetailsJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String prodottoParam = req.getParameter("prodotto");
        Integer prodotto = Integer.valueOf(prodottoParam);

        Prodotto prodottoObj;
        List<VersioneAggregata> versioniAggregate;
        try
        {
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            prodottoObj = new Prodotto();
            prodottoObj.setNumProgressivo(prodotto);
            prodottoObj.setCancellato(false);
            Optional<Prodotto> optional = prodottoDao.get(prodottoObj);
            if(optional.isEmpty())
                throw new ServletException("Prodotto non esistente");
            prodottoObj = optional.get();

            versioniAggregate = VersioneAggregata.getVersioniAggregate(prodotto);
        }
        catch(SQLException e)
        {
            throw new ServletException("Errore di comunicazione col database");
        }

        req.setAttribute("prodotto", prodottoObj);
        req.setAttribute("versioniAggregate", versioniAggregate);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(productDetailsJsp);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
