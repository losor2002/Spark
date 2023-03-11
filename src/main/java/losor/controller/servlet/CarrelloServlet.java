package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Carrello;
import losor.model.bean.Prodotto;
import losor.model.dao.Dao;
import losor.model.dao.ProdottoDao;
import losor.model.util.CarrelloUtil;
import losor.model.util.VersioneAggregata;

@WebServlet(urlPatterns = {"/carrello"},
            initParams = {@WebInitParam(name = "carrelloJsp", value = "/jsp/carrello.jsp")})
public class CarrelloServlet extends HttpServlet
{
    private String carrelloJsp;

    @Override
    public void init() throws ServletException
    {
        carrelloJsp = getInitParameter("carrelloJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String versione = req.getParameter("versione");
        String op = req.getParameter("op");

        HttpSession session = req.getSession();

        try
        {
            if(!ParameterUtil.areEmpty(versione, op))
            {
                if(op.equals("add"))
                    CarrelloUtil.add(session, versione);
                else if(op.equals("changeAmount"))
                {
                    String amountParam = req.getParameter("amount");
                    Integer amount = Integer.valueOf(amountParam);

                    CarrelloUtil.changeAmount(session, versione, amount);
                }
                else
                    throw new ServletException();

                resp.sendRedirect(req.getRequestURL().toString());
                return;
            }

            List<Carrello> carrello = CarrelloUtil.getAll(session);
            List<VersioneAggregata> versioniAggregate = new ArrayList<>();
            for(Carrello c : carrello)
                versioniAggregate.add(VersioneAggregata.getVersioneAggregata(c.getVersione()));
            List<Prodotto> prodotti = new ArrayList<>();
            Prodotto prodotto = new Prodotto();
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            for(VersioneAggregata versioneAggregata : versioniAggregate)
            {
                prodotto.setNumProgressivo(versioneAggregata.getVersione().getProdotto());
                prodotti.add(prodottoDao.get(prodotto).get());
            }
            req.setAttribute("carrello", carrello);
            req.setAttribute("versioniAggregate", versioniAggregate);
            req.setAttribute("prodotti", prodotti);
            forward(req, resp);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(carrelloJsp);
        dispatcher.forward(req, resp);
    }
}
