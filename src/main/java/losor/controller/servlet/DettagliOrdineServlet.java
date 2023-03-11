package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Cliente;
import losor.model.bean.Composizione;
import losor.model.bean.Indirizzo;
import losor.model.bean.Ordine;
import losor.model.bean.Prodotto;
import losor.model.bean.Versione;
import losor.model.dao.ClienteDao;
import losor.model.dao.ComposizioneDao;
import losor.model.dao.Dao;
import losor.model.dao.IndirizzoDao;
import losor.model.dao.OrdineDao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.VersioneDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/dettagliOrdine", "/dettagliOrdineAdmin"},
            initParams = {@WebInitParam(name = "dettagliOrdineJsp", value = "/jsp/dettagliOrdine.jsp")})
public class DettagliOrdineServlet extends HttpServlet
{
    private String dettagliOrdineJsp;

    @Override
    public void init() throws ServletException
    {
        dettagliOrdineJsp = getInitParameter("dettagliOrdineJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        boolean isAdmin = req.getServletPath().equals("/dettagliOrdineAdmin");

        String numFatturaParam = req.getParameter("numFattura");

        if(ParameterUtil.isEmpty(numFatturaParam))
            throw new ServletException();

        Integer numFattura = Integer.valueOf(numFatturaParam);

        try
        {
            Ordine ordine = new Ordine();
            ordine.setNumFattura(numFattura);
            if(!isAdmin)
                ordine.setCliente(UserUtil.getUserId(req.getSession()).get());
            Optional<Ordine> optional = OrdineDao.getInstance().get(ordine);

            if(optional.isEmpty())
                throw new ServletException();

            ordine = optional.get();

            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setNumProgressivo(ordine.getIndirizzo());
            indirizzo = IndirizzoDao.getInstance().get(indirizzo).get();

            Composizione composizione = new Composizione();
            composizione.setOrdine(numFattura);
            List<Composizione> composizioni = ComposizioneDao.getInstance().getAll(composizione);

            Versione versione = new Versione();
            List<Versione> versioni = new ArrayList<>();
            Dao<Versione> versioneDao = VersioneDao.getInstance();
            for(Composizione c : composizioni)
            {
                versione.setCodice(c.getVersione());
                versioni.add(versioneDao.get(versione).get());
            }

            Prodotto prodotto = new Prodotto();
            List<Prodotto> prodotti = new ArrayList<>();
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            for(Versione v : versioni)
            {
                prodotto.setNumProgressivo(v.getProdotto());
                prodotti.add(prodottoDao.get(prodotto).get());
            }

            if(isAdmin)
            {
                Cliente cliente = new Cliente();
                cliente.setCodice(ordine.getCliente());
                cliente = ClienteDao.getInstance().get(cliente).get();
                req.setAttribute("cliente", cliente);
            }

            req.setAttribute("ordine", ordine);
            req.setAttribute("indirizzo", indirizzo);
            req.setAttribute("composizioni", composizioni);
            req.setAttribute("versioni", versioni);
            req.setAttribute("prodotti", prodotti);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }
        
        if(isAdmin)
            req.setAttribute("admin", Boolean.TRUE);
        req.getRequestDispatcher(dettagliOrdineJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
