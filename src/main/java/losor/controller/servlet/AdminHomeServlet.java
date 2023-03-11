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
import javax.servlet.http.HttpSession;

import losor.model.bean.Amministratore;
import losor.model.bean.Prodotto;
import losor.model.dao.AmministratoreDao;
import losor.model.dao.Dao;
import losor.model.dao.ProdottoDao;
import losor.model.util.AdminUtil;

@WebServlet(urlPatterns = {"/adminHome"},
            initParams = {@WebInitParam(name = "adminHomeJsp", value = "/jsp/adminHome.jsp")})
public class AdminHomeServlet extends HttpServlet
{
    private String adminHomeJsp;

    @Override
    public void init() throws ServletException
    {
        adminHomeJsp = getInitParameter("adminHomeJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
        Prodotto template = new Prodotto();
        template.setCancellato(false);
        List<Prodotto> prodotti = null;

        try
        {
            prodotti = prodottoDao.getAll(template);
        }
        catch(SQLException e)
        {
            throw new ServletException("Impossibile prelevare la lista di prodotti dal database");
        }

        req.setAttribute("prodotti", prodotti);

        HttpSession session = req.getSession();

        Dao<Amministratore> dao = AmministratoreDao.getInstance();
        Amministratore admin = new Amministratore();
        admin.setCodice(AdminUtil.getAdminId(session).get());
        Optional<Amministratore> optional = null;

        try
        {
            optional = dao.get(admin);
        }
        catch(SQLException e)
        {
            forward(req, res);
            return;
        }

        if(optional.isEmpty())
        {
            AdminUtil.logout(session);
            throw new ServletException("L'amministratore è loggato ma non è presente nel Database");
        }

        admin = optional.get();
        req.setAttribute("nome", admin.getNome());
        req.setAttribute("cognome", admin.getCognome());
        forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(adminHomeJsp);
        dispatcher.forward(req, res);
    }
}