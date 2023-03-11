package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.controller.util.ResponseUtil;
import losor.model.bean.Carta;
import losor.model.dao.CartaDao;
import losor.model.dao.Dao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/deleteCarta"})
public class DeleteCartaServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String numero = req.getParameter("numero");

        if(ParameterUtil.isEmpty(numero))
            throw new ServletException();

        Carta carta = new Carta();
        carta.setNumero(numero);
        carta.setCliente(UserUtil.getUserId(req.getSession()).get());
        Dao<Carta> cartaDao = CartaDao.getInstance();

        try
        {
            synchronized(UserUtil.getUserLock(req.getSession()))
            {
                if(!cartaDao.exists(carta))
                    throw new ServletException();

                cartaDao.delete(carta);
            }
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        ResponseUtil.writeJsonResponse(resp, "{}");
    }
}
