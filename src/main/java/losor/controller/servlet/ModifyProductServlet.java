package losor.controller.servlet;

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

import javax.servlet.annotation.WebServlet;

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

@WebServlet(urlPatterns = {"/modifyProduct"},
            initParams = {@WebInitParam(name = "insertProductJsp", value = "/jsp/insertProduct.jsp"), @WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp"),
            @WebInitParam(name = "adminHome", value = "/adminHome")})

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 5, 
            maxRequestSize = 1024 * 1024 * 5 * 5)
public class ModifyProductServlet extends HttpServlet
{
    private String insertProductJsp, sparkFilesAbsolutePath, prodottoSalvatoJsp, adminHome;

    @Override
    public void init() throws ServletException
    {
        insertProductJsp = getInitParameter("insertProductJsp");
        sparkFilesAbsolutePath = getServletContext().getInitParameter("sparkFilesAbsolutePath");
        prodottoSalvatoJsp = getInitParameter("prodottoSalvatoJsp");
        adminHome = getServletContext().getContextPath() + getInitParameter("adminHome");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String numProgressivoParam = req.getParameter("numProgressivo");

        Integer numProgressivo;
        try
        {
            numProgressivo = Integer.valueOf(numProgressivoParam);
        }
        catch(NumberFormatException e)
        {
            resp.sendRedirect(adminHome);
            return;
        }

        Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
        Prodotto prodotto = new Prodotto();
        prodotto.setNumProgressivo(numProgressivo);
        prodotto.setCancellato(false);
        Optional<Prodotto> optional;

        try
        {
            optional = prodottoDao.get(prodotto);
        }
        catch(SQLException e)
        {
            resp.sendRedirect(adminHome);
            return;
        }

        if(optional.isEmpty())
        {
            resp.sendRedirect(adminHome);
            return;
        }

        prodotto = optional.get();

        Dao<Appartenenza> appartenenzaDao = AppartenenzaDao.getInstance();
        List<Appartenenza> appartenenze;

        try
        {
            Appartenenza appartenenza = new Appartenenza();
            appartenenza.setProdotto(prodotto.getNumProgressivo());
            appartenenze = appartenenzaDao.getAll(appartenenza);
        }
        catch(SQLException e)
        {
            resp.sendRedirect(adminHome);
            return;
        }

        Dao<Categoria> categoriaDao = CategoriaDao.getInstance();
        List<Categoria> categorie = new ArrayList<>();

        try
        {
            Categoria categoria = new Categoria();
            for(Appartenenza appartenenza : appartenenze)
            {
                categoria.setNumProgressivo(appartenenza.getCategoria());
                Optional<Categoria> optionalCat = categoriaDao.get(categoria);
                categorie.add(optionalCat.get());
            }
        }
        catch(SQLException e)
        {
            resp.sendRedirect(adminHome);
            return;
        }

        req.setAttribute("prodotto", prodotto);
        req.setAttribute("categorie", categorie);
        forward(req, resp);
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

        String numProgressivoParam = req.getParameter("numProgressivo");
        String nome = req.getParameter("nome");
        Part icona = req.getPart("icona");
        String produttoreParam = req.getParameter("produttore");
        String numCategorieParam = req.getParameter("numCategorie");

        if(ParameterUtil.areEmpty(numProgressivoParam, nome, produttoreParam, numCategorieParam))
        {
            forward(req, res);
            return;
        }

        Integer numProgressivo;
        try
        {
            numProgressivo = Integer.valueOf(numProgressivoParam);
        }
        catch(NumberFormatException e)
        {
            forward(req, res);
            return;
        }

        Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
        Prodotto prodotto = new Prodotto();
        prodotto.setNumProgressivo(numProgressivo);
        prodotto.setCancellato(false);
        try
        {
            Optional<Prodotto> optional = prodottoDao.get(prodotto);
            if(optional.isEmpty())
            {
                forward(req, res);
                return;
            }
            prodotto = optional.get();
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

        if(!prodotto.getNome().equals(nome))
        {
            try
            {
                Prodotto prodottoTemp = new Prodotto();
                prodottoTemp.setNome(nome);
                if(prodottoDao.exists(prodottoTemp))
                {
                    req.setAttribute("prodottoEsistente", Boolean.TRUE);
                    forward(req, res);
                    return;
                }
                prodotto.setNome(nome);
                prodottoDao.save(prodotto);
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        Dao<Appartenenza> appartenenzaDao = AppartenenzaDao.getInstance();
        List<Appartenenza> appartenenze;

        try
        {
            Appartenenza appartenenza = new Appartenenza();
            appartenenza.setProdotto(prodotto.getNumProgressivo());
            appartenenze = appartenenzaDao.getAll(appartenenza);
            appartenenzaDao.delete(appartenenza);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        Dao<Categoria> categoriaDao = CategoriaDao.getInstance();
        try
        {
            Categoria categoria = new Categoria();
            Appartenenza appartenenza = new Appartenenza();
            for(Appartenenza appartenenzaTemp : appartenenze)
            {
                appartenenza.setCategoria(appartenenzaTemp.getCategoria());
                if(!appartenenzaDao.exists(appartenenza))
                {
                    categoria.setNumProgressivo(appartenenza.getCategoria());
                    categoriaDao.delete(categoria);
                }
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        for(int i = 0; i < categorie.size(); i++)
        {
            Categoria categoria = categorie.get(i);
            try
            {
                Optional<Categoria> optional = categoriaDao.get(categoria);
                if(optional.isEmpty())
                {
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

        if(!prodotto.getProduttore().equals(produttoreParam))
        {
            Prodotto prodottoTemp = new Prodotto();
            prodottoTemp.setProduttore(prodotto.getProduttore());

            Dao<Produttore> produttoreDao = ProduttoreDao.getInstance();
            Produttore produttore = new Produttore(produttoreParam);
            try
            {
                if(!produttoreDao.exists(produttore))
                    produttoreDao.save(produttore);

                prodotto.setProduttore(produttoreParam);
                prodottoDao.save(prodotto);

                
                if(!prodottoDao.exists(prodottoTemp))
                    produttoreDao.delete(new Produttore(prodottoTemp.getProduttore()));
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        if(!PartUtil.isEmpty(icona))
        {
            File cartellaProdotto = new File(sparkFiles, prodotto.getNumProgressivo().toString());
            if(!cartellaProdotto.exists())
            {
                if(!cartellaProdotto.mkdir())
                    throw new ServletException("Impossibile creare la cartella in cui salvare le immagini del prodotto");
            }
            File iconaProdotto = new File(cartellaProdotto, "icona.jpg");
            icona.write(iconaProdotto.getAbsolutePath());
        }

        Categorie.invalidate();

        ProdottiByCategoria.invalidate();

        CategorieByProdotto.invalidate();

        RequestDispatcher dispatcher = req.getRequestDispatcher(prodottoSalvatoJsp);
        dispatcher.forward(req, res);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(insertProductJsp);
        dispatcher.forward(req, res);
    }
}
