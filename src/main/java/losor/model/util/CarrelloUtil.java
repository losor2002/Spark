package losor.model.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpSession;

import losor.model.bean.Carrello;
import losor.model.bean.DatiVersione;
import losor.model.bean.Versione;
import losor.model.dao.CarrelloDao;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.VersioneDao;

public abstract class CarrelloUtil
{
    @SuppressWarnings("unchecked")
    private static void changeAmount(HttpSession session, String codiceVersione, Function<Carrello, Integer> amount) throws SQLException
    {
        if(session == null || codiceVersione == null)
            throw new NullPointerException();

        Versione versione = new Versione();
        versione.setCodice(codiceVersione);
        versione.setCancellata(false);
        if(!VersioneDao.getInstance().exists(versione))
            throw new IllegalArgumentException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(UserUtil.isUserAuth(session))
            {
                synchronized(UserUtil.getUserLock(session))
                {
                    Carrello carrello = new Carrello();
                    carrello.setCliente(UserUtil.getUserId(session).get());
                    carrello.setVersione(codiceVersione);
                    Dao<Carrello> carrelloDao = CarrelloDao.getInstance();
                    Optional<Carrello> optional = carrelloDao.get(carrello);

                    if(optional.isEmpty())
                    {
                        carrello.setQuantita(0);

                        carrello.setQuantita(amount.apply(carrello));

                        if(carrello.getQuantita() > 0)
                            carrelloDao.save(carrello);
                    }
                    else
                    {
                        carrello = optional.get();
                        Integer quantita = amount.apply(carrello);

                        if(quantita > 0)
                        {
                            carrello.setQuantita(quantita);
                            carrelloDao.save(carrello);
                        }
                        else
                            carrelloDao.delete(carrello);
                    }
                }
            }
            else
            {
                List<Carrello> carrello = (List<Carrello>) session.getAttribute("carrello");
                if(carrello == null)
                {
                    carrello = new ArrayList<>();
                    session.setAttribute("carrello", carrello);
                }

                Optional<Carrello> optional = carrello.stream().filter(e -> e.getVersione().equals(codiceVersione)).findAny();

                if(optional.isEmpty())
                {
                    Carrello carrelloObj = new Carrello(codiceVersione, null, 0);
                    carrelloObj.setQuantita(amount.apply(carrelloObj));

                    if(carrelloObj.getQuantita() > 0)
                        carrello.add(carrelloObj);
                }
                else
                {
                    Carrello carrelloObj = optional.get();
                    Integer quantita = amount.apply(carrelloObj);

                    if(quantita > 0)
                        carrelloObj.setQuantita(quantita);
                    else
                        carrello.remove(carrelloObj);
                }
            }
        }
    }

    public static void changeAmount(HttpSession session, String codiceVersione, Integer amount) throws SQLException
    {
        changeAmount(session, codiceVersione, e -> amount);
    }

    public static void add(HttpSession session, String codiceVersione) throws SQLException
    {
        changeAmount(session, codiceVersione, e -> e.getQuantita() + 1);
    }

    @SuppressWarnings("unchecked")
    public static List<Carrello> getAll(HttpSession session) throws SQLException
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(UserUtil.isUserAuth(session))
            {
                synchronized(UserUtil.getUserLock(session))
                {
                    Dao<Carrello> carrelloDao = CarrelloDao.getInstance();
                    Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();

                    Carrello carrelloObj = new Carrello();
                    carrelloObj.setCliente(UserUtil.getUserId(session).get());
                    List<Carrello> carrello = carrelloDao.getAll(carrelloObj);

                    Iterator<Carrello> iterator = carrello.iterator();
                    while(iterator.hasNext())
                    {
                        Carrello c = iterator.next();

                        DatiVersione datiVersione = new DatiVersione();
                        datiVersione.setVersione(c.getVersione());
                        datiVersione = datiVersioneDao.get(datiVersione).get();
                        Integer quantita = datiVersione.getQuantita();

                        if(c.getQuantita() > quantita)
                        {
                            if(quantita > 0)
                            {
                                c.setQuantita(quantita);
                                carrelloDao.save(c);
                            }
                            else
                            {
                                iterator.remove();
                                carrelloDao.delete(c);
                            }
                        }
                    }

                    return carrello;
                }
            }
            else
            {
                List<Carrello> carrello = (List<Carrello>) session.getAttribute("carrello");
                if(carrello == null)
                {
                    carrello = new ArrayList<>();
                    session.setAttribute("carrello", carrello);
                    return carrello;
                }

                Iterator<Carrello> iterator = carrello.iterator();
                Dao<Versione> dao = VersioneDao.getInstance();
                Versione versione = new Versione();
                versione.setCancellata(false);
                while(iterator.hasNext())
                {
                    Carrello c = iterator.next();
                    versione.setCodice(c.getVersione());
                    if(!dao.exists(versione))
                        iterator.remove();
                }

                Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();

                iterator = carrello.iterator();
                while(iterator.hasNext())
                {
                    Carrello c = iterator.next();

                    DatiVersione datiVersione = new DatiVersione();
                    datiVersione.setVersione(c.getVersione());
                    datiVersione = datiVersioneDao.get(datiVersione).get();
                    Integer quantita = datiVersione.getQuantita();

                    if(c.getQuantita() > quantita)
                    {
                        if(quantita > 0)
                        {
                            c.setQuantita(quantita);
                        }
                        else
                        {
                            iterator.remove();
                        }
                    }
                }

                return carrello;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void transferToDB(HttpSession session) throws SQLException
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(!UserUtil.isUserAuth(session))
                throw new IllegalArgumentException();

            List<Carrello> carrello = (List<Carrello>) session.getAttribute("carrello");
            if(carrello == null || carrello.isEmpty())
                return;
            
            synchronized(UserUtil.getUserLock(session))
            {
                Dao<Carrello> carrelloDao = CarrelloDao.getInstance();
                Dao<Versione> versioneDao = VersioneDao.getInstance();
                Versione versione = new Versione();
                versione.setCancellata(false);
                Integer idCliente = UserUtil.getUserId(session).get();
                for(Carrello c : carrello)
                {
                    versione.setCodice(c.getVersione());
                    if(versioneDao.exists(versione))
                    {
                        c.setCliente(idCliente);
                        carrelloDao.save(c);
                    }
                }
            }
            emptySessionCart(session);
        }
    }

    public static void empty(HttpSession session) throws SQLException
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(UserUtil.isUserAuth(session))
            {
                synchronized(UserUtil.getUserLock(session))
                {
                    Dao<Carrello> carrelloDao = CarrelloDao.getInstance();
                    Carrello carrello = new Carrello();
                    carrello.setCliente(UserUtil.getUserId(session).get());
                    carrelloDao.delete(carrello);
                }
            }
            else
            {
                emptySessionCart(session);
            }
        }
    }

    public static void emptySessionCart(HttpSession session)
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            session.removeAttribute("carrello");
        }
    }
}
