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

import losor.model.bean.Carta;
import losor.model.dao.CartaDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/areaPersonale/carte"},
            initParams = {@WebInitParam(name = "carteJsp", value = "/jsp/carte.jsp")})
public class CarteServlet extends HttpServlet
{
    private String carteJsp;

    @Override
    public void init() throws ServletException
    {
        carteJsp = getInitParameter("carteJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Carta carta = new Carta();
        carta.setCliente(UserUtil.getUserId(req.getSession()).get());

        try
        {
            List<Carta> carte = CartaDao.getInstance().getAll(carta);
            req.setAttribute("carte", carte);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
        
        req.getRequestDispatcher(carteJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
