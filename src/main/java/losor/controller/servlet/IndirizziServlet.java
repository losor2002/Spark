package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.bean.Indirizzo;
import losor.model.dao.IndirizzoDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/areaPersonale/indirizzi"},
            initParams = {@WebInitParam(name = "indirizziJsp", value = "/jsp/indirizzi.jsp")})
public class IndirizziServlet extends HttpServlet
{
    private String indirizziJsp;

    @Override
    public void init() throws ServletException
    {
        indirizziJsp = getInitParameter("indirizziJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setCliente(UserUtil.getUserId(req.getSession()).get());

        try
        {
            List<Indirizzo> indirizzi = IndirizzoDao.getInstance().getAll(indirizzo);
            req.setAttribute("indirizzi", indirizzi);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
        
        req.getRequestDispatcher(indirizziJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
