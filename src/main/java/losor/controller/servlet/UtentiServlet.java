package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Cliente;
import losor.model.dao.ClienteDao;

@WebServlet(urlPatterns = {"/utenti"},
            initParams = {@WebInitParam(name = "utentiJsp", value = "/jsp/utenti.jsp")})
public class UtentiServlet extends HttpServlet
{
    private String utentiJsp;

    @Override
    public void init() throws ServletException
    {
        utentiJsp = getInitParameter("utentiJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String email = req.getParameter("email");

        List<Cliente> clienti;
        try
        {
            clienti = ClienteDao.getInstance().getAll(null);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        if(ParameterUtil.isEmpty(email))
        {
            clienti = clienti.parallelStream().sorted(Comparator.comparing(Cliente::getCodice)).toList();
        }
        else
        {
            clienti = clienti.parallelStream().filter(e -> e.getEmail().equals(email)).toList();
        }

        req.setAttribute("clienti", clienti);
        req.getRequestDispatcher(utentiJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
