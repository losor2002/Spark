package losor.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.bean.Appartenenza;
import losor.model.bean.Categoria;
import losor.model.bean.Prodotto;
import losor.model.bean.Produttore;
import losor.model.bean.Versione;
import losor.model.dao.AppartenenzaDao;
import losor.model.dao.CategoriaDao;
import losor.model.dao.Dao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.ProduttoreDao;
import losor.model.dao.VersioneDao;
import losor.model.util.Categorie;
import losor.model.util.CategorieByProdotto;
import losor.model.util.ProdottiByCategoria;

@WebServlet(urlPatterns = {"/deleteProduct"},
            initParams = {@WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp"), @WebInitParam(name = "deleteVersion", value = "/deleteVersion")})
public class DeleteProductServlet extends HttpServlet
{
    private String prodottoSalvatoJsp, sparkFilesAbsolutePath, deleteVersion;

    @Override
    public void init() throws ServletException
    {
        prodottoSalvatoJsp = getInitParameter("prodottoSalvatoJsp");
        sparkFilesAbsolutePath = getServletContext().getInitParameter("sparkFilesAbsolutePath");
        deleteVersion = getInitParameter("deleteVersion");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setAttribute("delete", Boolean.TRUE);

        String numProgressivoParam = req.getParameter("numProgressivo");
        Integer numProgressivo = Integer.valueOf(numProgressivoParam);
        
        try
        {
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            Prodotto prodotto = new Prodotto();
            prodotto.setNumProgressivo(numProgressivo);
            prodotto.setCancellato(false);
            Optional<Prodotto> optional = prodottoDao.get(prodotto);
            if(optional.isEmpty())
                throw new ServletException();
            prodotto = optional.get();

            Dao<Versione> versioneDao = VersioneDao.getInstance();
            Versione versione = new Versione();
            versione.setProdotto(numProgressivo);
            versione.setCancellata(false);
            List<Versione> versioni = versioneDao.getAll(versione);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher(deleteVersion);
            for(Versione v : versioni)
            {
                req.setAttribute("codice", v.getCodice());
                requestDispatcher.include(req, resp);
                SQLException exception = (SQLException) req.getAttribute("SQLException");
                if(exception != null)
                    throw exception;
            }

            Dao<Appartenenza> appartenenzaDao = AppartenenzaDao.getInstance();
            Appartenenza appartenenza = new Appartenenza();
            appartenenza.setProdotto(numProgressivo);
            List<Appartenenza> appartenenze = appartenenzaDao.getAll(appartenenza);
            appartenenzaDao.delete(appartenenza);

            Dao<Categoria> categoriaDao = CategoriaDao.getInstance();
            Categoria categoria = new Categoria();
            appartenenza.setProdotto(null);
            for(Appartenenza a : appartenenze)
            {
                appartenenza.setCategoria(a.getCategoria());
                if(!appartenenzaDao.exists(appartenenza))
                {
                    categoria.setNumProgressivo(appartenenza.getCategoria());
                    categoriaDao.delete(categoria);
                }
            }

            versione.setCancellata(null);
            if(!versioneDao.exists(versione))
            {
                prodottoDao.delete(prodotto);

                Prodotto prodotto2 = new Prodotto();
                prodotto2.setProduttore(prodotto.getProduttore());
                if(!prodottoDao.exists(prodotto2))
                {
                    Dao<Produttore> produttoreDao = ProduttoreDao.getInstance();
                    Produttore produttore = new Produttore(prodotto.getProduttore());
                    produttoreDao.delete(produttore);
                }

                File prodottoDir = new File(sparkFilesAbsolutePath, numProgressivo.toString());
                try(Stream<Path> paths = Files.walk(prodottoDir.toPath()))
                {
                    paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
            }
            else
            {
                prodotto.setCancellato(true);
                prodotto.setIcona(null);
                prodottoDao.save(prodotto);

                File icona = new File(sparkFilesAbsolutePath, numProgressivo + File.separator + "icona.jpg");
                icona.delete();
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, resp);
            return;
        }

        Categorie.invalidate();

        ProdottiByCategoria.invalidate();

        CategorieByProdotto.invalidate();

        forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(prodottoSalvatoJsp);
        dispatcher.forward(req, res);
    }
}
