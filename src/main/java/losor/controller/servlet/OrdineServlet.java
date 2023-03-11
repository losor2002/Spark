package losor.controller.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Carrello;
import losor.model.bean.Carta;
import losor.model.bean.Cliente;
import losor.model.bean.Composizione;
import losor.model.bean.DatiVersione;
import losor.model.bean.Indirizzo;
import losor.model.bean.Ordine;
import losor.model.bean.Prodotto;
import losor.model.dao.CartaDao;
import losor.model.dao.ClienteDao;
import losor.model.dao.ComposizioneDao;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.IndirizzoDao;
import losor.model.dao.OrdineDao;
import losor.model.dao.ProdottoDao;
import losor.model.util.CarrelloUtil;
import losor.model.util.UserUtil;
import losor.model.util.VersioneAggregata;

@WebServlet(urlPatterns = {"/ordine"},
            initParams = {@WebInitParam(name = "ordineJsp", value = "/jsp/ordine.jsp"), @WebInitParam(name = "orderSuccessJsp", value = "/jsp/orderSuccess.jsp")})
public class OrdineServlet extends HttpServlet
{
    private String ordineJsp, orderSuccessJsp;

    @Override
    public void init() throws ServletException
    {
        ordineJsp = getInitParameter("ordineJsp");
        orderSuccessJsp = getInitParameter("orderSuccessJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            HttpSession session = req.getSession();

            List<Carrello> carrello = CarrelloUtil.getAll(session);

            if(carrello.isEmpty())
                throw new ServletException();

            List<VersioneAggregata> versioniAggregate = new ArrayList<>();
            for(Carrello c : carrello)
                versioniAggregate.add(VersioneAggregata.getVersioneAggregata(c.getVersione()));
            List<Prodotto> prodotti = new ArrayList<>();
            Prodotto prodotto = new Prodotto();
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            for(VersioneAggregata versioneAggregata : versioniAggregate)
            {
                prodotto.setNumProgressivo(versioneAggregata.getVersione().getProdotto());
                prodotti.add(prodottoDao.get(prodotto).get());
            }

            Integer cliente = UserUtil.getUserId(session).get();

            Carta carta = new Carta();
            carta.setCliente(cliente);
            List<Carta> carte = CartaDao.getInstance().getAll(carta);

            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setCliente(cliente);
            List<Indirizzo> indirizzi = IndirizzoDao.getInstance().getAll(indirizzo);

            req.setAttribute("carrello", carrello);
            req.setAttribute("versioniAggregate", versioniAggregate);
            req.setAttribute("prodotti", prodotti);
            req.setAttribute("carte", carte);
            req.setAttribute("indirizzi", indirizzi);
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        req.getRequestDispatcher(ordineJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String numCarta = req.getParameter("carta");
        String numIndirizzoParam = req.getParameter("indirizzo");

        if(ParameterUtil.areEmpty(numCarta, numIndirizzoParam))
            throw new ServletException();

        if(!numCarta.matches("^\\d{13,16}$"))
            throw new ServletException();
        
        Integer numIndirizzo = Integer.valueOf(numIndirizzoParam);

        HttpSession session = req.getSession();
        Integer cliente = UserUtil.getUserId(session).get();

        try
        {
            Carta carta = new Carta();
            carta.setCliente(cliente);
            carta.setNumero(numCarta);
            if(!CartaDao.getInstance().exists(carta))
                throw new ServletException();

            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setCliente(cliente);
            indirizzo.setNumProgressivo(numIndirizzo);
            if(!IndirizzoDao.getInstance().exists(indirizzo))
                throw new ServletException();

            synchronized(UserUtil.getUserLock(session))
            {
                List<Carrello> carrello;
                List<DatiVersione> datiVersioni;

                synchronized(OrdineServlet.class)
                {
                    carrello = CarrelloUtil.getAll(session);
                    if(carrello.isEmpty())
                        throw new ServletException();

                    datiVersioni = new ArrayList<>();
                    DatiVersione datiVersione = new DatiVersione();
                    Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();
                    for(Carrello c : carrello)
                    {
                        datiVersione.setVersione(c.getVersione());
                        datiVersioni.add(datiVersioneDao.get(datiVersione).get());
                    }

                    for(int i = 0; i < carrello.size(); i++)
                    {
                        Carrello c = carrello.get(i);
                        DatiVersione d = datiVersioni.get(i);

                        d.setQuantita(d.getQuantita() - c.getQuantita());
                        datiVersioneDao.save(d);
                    }
                }

                BigDecimal totale = new BigDecimal(0);
                for(int i = 0; i < carrello.size(); i++)
                {
                    BigDecimal quantita = new BigDecimal(carrello.get(i).getQuantita());
                    BigDecimal prezzo = datiVersioni.get(i).getPrezzo();
                    totale = totale.add(prezzo.multiply(quantita));
                }

                Ordine ordine = new Ordine(null, numCarta, new Date(System.currentTimeMillis()), totale, numIndirizzo, cliente);
                Dao<Ordine> ordineDao = OrdineDao.getInstance();
                ordineDao.save(ordine);
                ordine = ordineDao.getAll(ordine).stream().sorted(Comparator.comparing(Ordine::getNumFattura).reversed()).findFirst().get();

                Dao<Composizione> composizioneDao = ComposizioneDao.getInstance();
                Composizione composizione = new Composizione();
                composizione.setOrdine(ordine.getNumFattura());
                for(int i = 0; i < carrello.size(); i++)
                {
                    Carrello c = carrello.get(i);
                    DatiVersione d = datiVersioni.get(i);

                    composizione.setVersione(c.getVersione());
                    composizione.setQuantita(c.getQuantita());
                    composizione.setPrezzo(d.getPrezzo());
                    composizione.setIva(d.getIva());

                    composizioneDao.save(composizione);
                }

                CarrelloUtil.empty(session);

                Cliente clienteObj = new Cliente();
                clienteObj.setCodice(cliente);
                Dao<Cliente> clienteDao = ClienteDao.getInstance();
                clienteObj = clienteDao.get(clienteObj).get();
                clienteObj.setNumAcquisti(clienteObj.getNumAcquisti() + 1);
                clienteDao.save(clienteObj);

                req.setAttribute("numeroFattura", ordine.getNumFattura());
            }
        }
        catch(SQLException e)
        {
            throw new ServletException();
        }

        req.getRequestDispatcher(orderSuccessJsp).forward(req, resp);
    }
}
