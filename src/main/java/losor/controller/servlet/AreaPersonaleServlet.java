package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.bean.Cliente;
import losor.model.dao.ClienteDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/areaPersonale"},
            initParams = {@WebInitParam(name = "areaPersonaleJsp", value = "/jsp/areaPersonale.jsp")})
public class AreaPersonaleServlet extends HttpServlet
{
    private String areaPersonaleJsp;

    @Override
    public void init() throws ServletException
    {
        areaPersonaleJsp = getInitParameter("areaPersonaleJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Cliente cliente = new Cliente();
        cliente.setCodice(UserUtil.getUserId(req.getSession()).get());

        try
        {
            cliente = ClienteDao.getInstance().get(cliente).get();
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        req.setAttribute("nome", cliente.getNome());
        req.setAttribute("cognome", cliente.getCognome());
        req.setAttribute("email", cliente.getEmail());
        
        req.getRequestDispatcher(areaPersonaleJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
