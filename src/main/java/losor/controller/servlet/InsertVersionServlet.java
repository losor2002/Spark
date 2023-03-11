package losor.controller.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import losor.controller.util.ParameterUtil;
import losor.controller.util.PartUtil;
import losor.model.bean.DatiVersione;
import losor.model.bean.Immagine;
import losor.model.bean.Prodotto;
import losor.model.bean.Versione;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.ImmagineDao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.VersioneDao;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/insertVersion"},
            initParams = {@WebInitParam(name = "insertVersionJsp", value = "/jsp/insertVersion.jsp"), @WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp")})

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 5, 
            maxRequestSize = 1024 * 1024 * 5 * 5)
public class InsertVersionServlet extends HttpServlet
{
    private String insertVersionJsp, sparkFilesAbsolutePath, prodottoSalvatoJsp;

    @Override
    public void init() throws ServletException
    {
        insertVersionJsp = getInitParameter("insertVersionJsp");
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

        String prodottoParam = req.getParameter("prodotto");
        String codice = req.getParameter("codice");
        String nome = req.getParameter("nome");
        Part icona = req.getPart("icona");
        String dataDiUscitaParam = req.getParameter("dataDiUscita");
        String descrizione = req.getParameter("descrizione");
        String ivaParam = req.getParameter("iva");
        String prezzoParam = req.getParameter("prezzo");
        String quantitaParam = req.getParameter("quantita");
        String numImmaginiParam = req.getParameter("numImmagini");

        if(PartUtil.isEmpty(icona) || ParameterUtil.areEmpty(prodottoParam, codice, nome, dataDiUscitaParam, descrizione, ivaParam, prezzoParam, quantitaParam, numImmaginiParam))
        {
            forward(req, res);
            return;
        }

        Integer prodotto = Integer.valueOf(prodottoParam);
        try
        {
            Dao<Prodotto> prodottoDao = ProdottoDao.getInstance();
            Prodotto prodottoObj = new Prodotto();
            prodottoObj.setNumProgressivo(prodotto);
            prodottoObj.setCancellato(false);
            if(!prodottoDao.exists(prodottoObj))
            {
                req.setAttribute("prodottoNonEsistente", Boolean.TRUE);
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

        try
        {
            Dao<Versione> versioneDao = VersioneDao.getInstance();
            Versione versione = new Versione();
            versione.setCodice(codice);
            if(versioneDao.exists(versione))
            {
                req.setAttribute("versioneEsistente", Boolean.TRUE);
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

        Date dataDiUscita = Date.valueOf(dataDiUscitaParam);

        BigDecimal iva;
        try
        {
            iva = new BigDecimal(ivaParam);
        }
        catch(NumberFormatException e)
        {
            req.setAttribute("ivaErrata", Boolean.TRUE);
            forward(req, res);
            return;
        }

        BigDecimal prezzo;
        try
        {
            prezzo = new BigDecimal(prezzoParam);
        }
        catch(NumberFormatException e)
        {
            req.setAttribute("prezzoErrato", Boolean.TRUE);
            forward(req, res);
            return;
        }

        Integer quantita = Integer.valueOf(quantitaParam);

        int numImmagini = Integer.valueOf(numImmaginiParam);
        if(numImmagini <= 0)
        {
            req.setAttribute("almenoUnaImmagine", Boolean.FALSE);
            forward(req, res);
            return;
        }

        List<Part> immagini = new ArrayList<>();
        for(int i = 0; i < numImmagini; i++)
        {
            Part immagine = req.getPart("immagine" + i);
            if(PartUtil.isEmpty(immagine))
            {
                forward(req, res);
                return;
            }
            immagini.add(immagine);
        }

        try
        {
            String iconaPath = "/" + prodotto + "/" + codice + "/icona.jpg";
            Dao<Versione> versioneDao = VersioneDao.getInstance();
            Versione versione = new Versione(codice, nome, iconaPath, false, prodotto);
            versioneDao.save(versione);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        try
        {
            Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();
            DatiVersione datiVersione = new DatiVersione(codice, dataDiUscita, descrizione, iva, prezzo, quantita);
            datiVersioneDao.save(datiVersione);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        try
        {
            Dao<Immagine> immagineDao = ImmagineDao.getInstance();
            for(int i = 0; i < numImmagini; i++)
            {
                String imagePath = "/" + prodotto + "/" + codice + "/" + i + ".jpg";
                Immagine immagine = new Immagine(codice, i, imagePath);
                immagineDao.save(immagine);
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        File cartellaVersione = new File(sparkFiles, prodotto + File.separator + codice);
        if(!cartellaVersione.exists())
        {
            if(!cartellaVersione.mkdirs())
                throw new ServletException("Impossibile creare la cartella della versione");
        }
        File iconaVersione = new File(cartellaVersione, "icona.jpg");
        icona.write(iconaVersione.getAbsolutePath());

        for(int i = 0; i < numImmagini; i++)
        {
            File immaginePath = new File(cartellaVersione, i + ".jpg");
            immagini.get(i).write(immaginePath.getAbsolutePath());
        }

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
        RequestDispatcher dispatcher = req.getRequestDispatcher(insertVersionJsp);
        dispatcher.forward(req, res);
    }
}
