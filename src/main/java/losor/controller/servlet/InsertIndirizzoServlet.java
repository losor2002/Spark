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
import losor.model.bean.Indirizzo;
import losor.model.dao.Dao;
import losor.model.dao.IndirizzoDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/insertIndirizzo"})
public class InsertIndirizzoServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String via = req.getParameter("via");
        String numCivicoParam = req.getParameter("numCivico");
        String cap = req.getParameter("cap");

        if(ParameterUtil.areEmpty(nome, cognome, via, numCivicoParam, cap))
            throw new ServletException();

        Integer numCivico = Integer.valueOf(numCivicoParam);

        try
        {
            Indirizzo indirizzo = new Indirizzo(null, numCivico, via, cap, nome, cognome, UserUtil.getUserId(req.getSession()).get());
            Dao<Indirizzo> dao = IndirizzoDao.getInstance();

            if(dao.exists(indirizzo))
            {
                ResponseUtil.writeJsonErrorResponse(resp, "L'indirizzo è già salvato nell'account");
                return;
            }

            dao.save(indirizzo);
            indirizzo = dao.get(indirizzo).get();
            ResponseUtil.writeJsonResponse(resp, indirizzo);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
    }
}
