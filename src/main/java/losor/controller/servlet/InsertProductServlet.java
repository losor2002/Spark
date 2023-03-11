package losor.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import losor.controller.util.ParameterUtil;
import losor.controller.util.PartUtil;
import losor.model.bean.Appartenenza;
import losor.model.bean.Categoria;
import losor.model.bean.Prodotto;
import losor.model.bean.Produttore;
import losor.model.dao.AppartenenzaDao;
import losor.model.dao.CategoriaDao;
import losor.model.dao.Dao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.ProduttoreDao;
import losor.model.util.Categorie;
import losor.model.util.CategorieByProdotto;
import losor.model.util.ProdottiByCategoria;

@WebServlet(urlPatterns = {"/insertProduct"},
            initParams = {@WebInitParam(name = "insertProductJsp", value = "/jsp/insertProduct.jsp"), @WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp")})

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 5, 
            maxRequestSize = 1024 * 1024 * 5 * 5)
public class InsertProductServlet extends HttpServlet
{
    private String insertProductJsp, sparkFilesAbsolutePath, prodottoSalvatoJsp;

    @Override
    public void init() throws ServletException
    {
        insertProductJsp = getInitParameter("insertProductJsp");
        sparkFilesAbsolutePath = getServletContext().getInitParameter("sparkFilesAbsolutePath");
        prodottoSalvatoJsp = getInitParameter("prodottoSalvatoJsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        File sparkFiles = new File(sparkFilesAbsolutePath);
        if(!sparkFiles.exists())
        {
            if(!sparkFiles.mkdir())
                throw new ServletException("Impossibile creare la cartella sparkFiles");
        }

        req.setCharacterEncoding("UTF-8");

        String nome = req.getParameter("nome");
        Part icona = req.getPart("icona");
        String produttoreParam = req.getParameter("produttore");
        String numCategorieParam = req.getParameter("numCategorie");

        if(PartUtil.isEmpty(icona) || ParameterUtil.areEmpty(nome, produttoreParam, numCategorieParam))
        {
            forward(req, res);
            return;
        }

        Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nome);
        try
        {
            if(prodottoDao.exists(prodotto))
            {
                req.setAttribute("prodottoEsistente", Boolean.TRUE);
                forward(req, res);
                return;
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        int numCategorie;
        try
        {
            numCategorie = Integer.valueOf(numCategorieParam);
        }
        catch(NumberFormatException e)
        {
            forward(req, res);
            return;
        }
        if(numCategorie <= 0)
        {
            forward(req, res);
            return;
        }

        List<Categoria> categorie = new ArrayList<>();
        for(int i = 0; i < numCategorie; i++)
        {
            String tipo = req.getParameter("tipo" + i);
            if(ParameterUtil.isEmpty(tipo))
            {
                forward(req, res);
                return;
            }

            String sottotipo = req.getParameter("sottotipo" + i);
            if(ParameterUtil.isEmpty(sottotipo))
            {
                forward(req, res);
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setTipo(tipo);
            categoria.setSottotipo(sottotipo);
            categorie.add(categoria);
        }

        Dao<Categoria> categoriaDao = CategoriaDao.getInstance();
        boolean nuovaCategoria = false;
        for(int i = 0; i < categorie.size(); i++)
        {
            Categoria categoria = categorie.get(i);
            try
            {
                Optional<Categoria> optional = categoriaDao.get(categoria);
                if(optional.isEmpty())
                {
                    nuovaCategoria = true;
                    categoriaDao.save(categoria);
                    optional = categoriaDao.get(categoria);
                }
                categorie.set(i, optional.get());
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        Dao<Produttore> produttoreDao = ProduttoreDao.getInstance();
        Produttore produttore = new Produttore(produttoreParam);
        try
        {
            if(!produttoreDao.exists(produttore))
                produttoreDao.save(produttore);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        prodotto.setCancellato(false);
        prodotto.setProduttore(produttore.getNome());
        try
        {
            prodottoDao.save(prodotto);
            prodotto = prodottoDao.get(prodotto).get();
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        File cartellaProdotto = new File(sparkFiles, prodotto.getNumProgressivo().toString());
        if(!cartellaProdotto.mkdir())
            throw new ServletException("Impossibile creare la cartella in cui salvare le immagini del prodotto");
        File iconaProdotto = new File(cartellaProdotto, "icona.jpg");
        icona.write(iconaProdotto.getAbsolutePath());
        
        prodotto.setIcona("/" + prodotto.getNumProgressivo().toString() + "/icona.jpg");
        try
        {
            prodottoDao.save(prodotto);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        Dao<Appartenenza> appartenenzaDao = AppartenenzaDao.getInstance();
        for(Categoria categoria : categorie)
        {
            Appartenenza appartenenza = new Appartenenza(prodotto.getNumProgressivo(), categoria.getNumProgressivo());
            try
            {
                appartenenzaDao.save(appartenenza);
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        if(nuovaCategoria)
            Categorie.invalidate();

        ProdottiByCategoria.invalidate();

        CategorieByProdotto.invalidate();

        RequestDispatcher dispatcher = req.getRequestDispatcher(prodottoSalvatoJsp);
        dispatcher.forward(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(insertProductJsp);
        dispatcher.forward(req, res);
    }
}
