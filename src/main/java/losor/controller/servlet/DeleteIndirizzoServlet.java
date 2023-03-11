package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.controller.util.ResponseUtil;
import losor.model.bean.Indirizzo;
import losor.model.bean.Ordine;
import losor.model.dao.Dao;
import losor.model.dao.IndirizzoDao;
import losor.model.dao.OrdineDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/deleteIndirizzo"})
public class DeleteIndirizzoServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String numProgressivoParam = req.getParameter("numProgressivo");

        if(ParameterUtil.isEmpty(numProgressivoParam))
            throw new ServletException();

        Integer numProgressivo = Integer.valueOf(numProgressivoParam);

        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setNumProgressivo(numProgressivo);
        indirizzo.setCliente(UserUtil.getUserId(req.getSession()).get());
        Dao<Indirizzo> indirizzoDao = IndirizzoDao.getInstance();

        try
        {
            synchronized(UserUtil.getUserLock(req.getSession()))
            {
                Optional<Indirizzo> optional = indirizzoDao.get(indirizzo);

                if(optional.isEmpty())
                    throw new ServletException();

                indirizzo = optional.get();

                Ordine ordine = new Ordine();
                ordine.setIndirizzo(numProgressivo);

                if(OrdineDao.getInstance().exists(ordine))
                {
                    indirizzo.setCliente(null);
                    indirizzoDao.save(indirizzo);
                }
                else
                {
                    indirizzoDao.delete(indirizzo);
                }
            }
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        ResponseUtil.writeJsonResponse(resp, "{}");
    }
}
