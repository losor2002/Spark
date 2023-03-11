package losor.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.bean.Carrello;
import losor.model.bean.Composizione;
import losor.model.bean.DatiVersione;
import losor.model.bean.Recensione;
import losor.model.bean.Versione;
import losor.model.bean.Wishlist;
import losor.model.dao.CarrelloDao;
import losor.model.dao.ComposizioneDao;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.RecensioneDao;
import losor.model.dao.VersioneDao;
import losor.model.dao.WishlistDao;
import losor.model.util.VersioneAggregata;

@WebServlet(urlPatterns = {"/deleteVersion"},
            initParams = {@WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp")})
public class DeleteVersionServlet extends HttpServlet
{
    private String prodottoSalvatoJsp, sparkFilesAbsolutePath;

    @Override
    public void init() throws ServletException
    {
        prodottoSalvatoJsp = getInitParameter("prodottoSalvatoJsp");
        sparkFilesAbsolutePath = getServletContext().getInitParameter("sparkFilesAbsolutePath");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setAttribute("delete", Boolean.TRUE);

        resp.setContentType("text/html; charset=UTF-8");

        String codice = (String) req.getAttribute("codice");
        if(codice == null)
            codice = req.getParameter("codice");

        try
        {
            VersioneAggregata versioneAggregata = VersioneAggregata.getVersioneAggregata(codice);

            Dao<Composizione> composizioneDao = ComposizioneDao.getInstance();
            Composizione composizione = new Composizione();
            composizione.setVersione(codice);
            if(!composizioneDao.exists(composizione))
            {
                Dao<Versione> versioneDao = VersioneDao.getInstance();
                versioneDao.delete(versioneAggregata.getVersione());
                File versionDirectory = new File(sparkFilesAbsolutePath, versioneAggregata.getVersione().getProdotto() + File.separator + codice);
                try(Stream<Path> paths = Files.walk(versionDirectory.toPath()))
                {
                    paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
                include(req, resp);
                return;
            }

            Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();
            datiVersioneDao.delete(versioneAggregata.getDatiVersione());

            Dao<Versione> versioneDao = VersioneDao.getInstance();
            versioneAggregata.getVersione().setCancellata(true);
            versioneDao.save(versioneAggregata.getVersione());

            Dao<Carrello> carrelloDao = CarrelloDao.getInstance();
            Carrello carrello = new Carrello();
            carrello.setVersione(codice);
            carrelloDao.delete(carrello);

            Dao<Wishlist> wishlistDao = WishlistDao.getInstance();
            Wishlist wishlist = new Wishlist();
            wishlist.setVersione(codice);
            wishlistDao.delete(wishlist);

            Dao<Recensione> recensioneDao = RecensioneDao.getInstance();
            Recensione recensione = new Recensione();
            recensione.setVersione(codice);
            recensioneDao.delete(recensione);

            File versionDirectory = new File(sparkFilesAbsolutePath, versioneAggregata.getVersione().getProdotto() + File.separator + codice);
            for(int i = 0; i < versioneAggregata.getImmagini().size(); i++)
            {
                File file = new File(versionDirectory, i + ".jpg");
                file.delete();
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            include(req, resp);
            return;
        }

        include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void include(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(prodottoSalvatoJsp);
        dispatcher.include(req, res);
    }
}
