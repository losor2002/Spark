package losor.controller.servlet;

import java.io.IOException;
import java.sql.Date;
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

@WebServlet(urlPatterns = {"/insertCarta"})
public class InsertCartaServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String numero = req.getParameter("numero");
        String scadenzaParam = req.getParameter("scadenza");
        String cvc = req.getParameter("cvc");

        if(ParameterUtil.areEmpty(nome, cognome, numero, scadenzaParam, cvc))
            throw new ServletException();

        Date scadenza = Date.valueOf(scadenzaParam);

        try
        {
            Carta carta = new Carta();
            carta.setNumero(numero);
            Dao<Carta> dao = CartaDao.getInstance();

            if(dao.exists(carta))
            {
                ResponseUtil.writeJsonErrorResponse(resp, "La carta è già salvata in questo o in un altro account");
                return;
            }

            carta.setNome(nome);
            carta.setCognome(cognome);
            carta.setScadenza(scadenza);
            carta.setCvc(cvc);
            carta.setCliente(UserUtil.getUserId(req.getSession()).get());

            dao.save(carta);
            ResponseUtil.writeJsonResponse(resp, carta);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
    }
}
