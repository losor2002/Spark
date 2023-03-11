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

import losor.model.bean.Ordine;
import losor.model.dao.OrdineDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/areaPersonale/ordini"},
            initParams = {@WebInitParam(name = "ordiniJsp", value = "/jsp/ordini.jsp")})
public class OrdiniServlet extends HttpServlet
{
    private String ordiniJsp;

    @Override
    public void init() throws ServletException
    {
        ordiniJsp = getInitParameter("ordiniJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Ordine ordine = new Ordine();
        ordine.setCliente(UserUtil.getUserId(req.getSession()).get());

        try
        {
            List<Ordine> ordini = OrdineDao.getInstance().getAll(ordine).stream().sorted(Comparator.comparing(Ordine::getNumFattura).reversed()).toList();
            req.setAttribute("ordini", ordini);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
        
        req.getRequestDispatcher(ordiniJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
