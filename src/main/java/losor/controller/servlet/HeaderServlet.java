package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.model.bean.Cliente;
import losor.model.dao.ClienteDao;
import losor.model.dao.Dao;
import losor.model.util.Categorie;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/header"},
            initParams = {@WebInitParam(name = "headerJsp", value = "/jsp/header.jsp")})
public class HeaderServlet extends HttpServlet
{
    private String headerJsp;

    @Override
    public void init() throws ServletException
    {
        headerJsp = getInitParameter("headerJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        req.setAttribute("categorie", Categorie.getInstance());

        HttpSession session = req.getSession();

        if(!UserUtil.isUserAuth(session))
        {
            req.setAttribute("isUserAuth", Boolean.FALSE);
            include(req, res);
            return;
        }

        req.setAttribute("isUserAuth", Boolean.TRUE);

        Dao<Cliente> dao = ClienteDao.getInstance();
        Cliente cliente = new Cliente();
        cliente.setCodice(UserUtil.getUserId(session).get());
        Optional<Cliente> optional = null;

        try
        {
            optional = dao.get(cliente);
        }
        catch(SQLException e)
        {
            include(req, res);
            return;
        }

        if(optional.isEmpty())
        {
            UserUtil.logout(session);
            throw new ServletException("L'utente è loggato ma non è presente nel Database");
        }

        cliente = optional.get();
        req.setAttribute("nome", cliente.getNome());
        req.setAttribute("cognome", cliente.getCognome());
        include(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void include(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(headerJsp);
        dispatcher.include(req, res);
    }
}
