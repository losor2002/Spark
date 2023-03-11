package losor.controller.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import losor.controller.util.ParameterUtil;
import losor.controller.util.PartUtil;
import losor.model.bean.DatiVersione;
import losor.model.bean.Immagine;
import losor.model.bean.Versione;
import losor.model.dao.Dao;
import losor.model.dao.DatiVersioneDao;
import losor.model.dao.ImmagineDao;
import losor.model.dao.VersioneDao;
import losor.model.util.VersioneAggregata;

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

@WebServlet(urlPatterns = {"/modifyVersion"},
            initParams = {@WebInitParam(name = "insertVersionJsp", value = "/jsp/insertVersion.jsp"), @WebInitParam(name = "prodottoSalvatoJsp", value = "/jsp/prodottoSalvato.jsp")})

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 5, 
            maxRequestSize = 1024 * 1024 * 5 * 5)
public class ModifyVersionServlet extends HttpServlet
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
        req.setAttribute("modifyVersion", Boolean.TRUE);

        File sparkFiles = new File(sparkFilesAbsolutePath);
        if(!sparkFiles.exists())
        {
            if(!sparkFiles.mkdir())
                throw new ServletException("Impossibile creare la cartella sparkFiles");
        }

        req.setCharacterEncoding("UTF-8");

        String codice = req.getParameter("codice");
        String nome = req.getParameter("nome");
        Part icona = req.getPart("icona");
        String dataDiUscitaParam = req.getParameter("dataDiUscita");
        String descrizione = req.getParameter("descrizione");
        String ivaParam = req.getParameter("iva");
        String prezzoParam = req.getParameter("prezzo");
        String quantitaParam = req.getParameter("quantita");
        String numImmaginiParam = req.getParameter("numImmagini");

        if(ParameterUtil.areEmpty(codice, nome, dataDiUscitaParam, descrizione, ivaParam, prezzoParam, quantitaParam, numImmaginiParam))
        {
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

        VersioneAggregata versioneAggregata;
        try
        {
            versioneAggregata = VersioneAggregata.getVersioneAggregata(codice);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        List<Part> immagini = new ArrayList<>();
        for(int i = 0; i < numImmagini; i++)
        {
            Part immagine = req.getPart("immagine" + i);
            if((i >= versioneAggregata.getImmagini().size()) && PartUtil.isEmpty(immagine))
            {
                forward(req, res);
                return;
            }
            immagini.add(immagine);
        }

        if(!nome.equals(versioneAggregata.getVersione().getNome()))
        {
            versioneAggregata.getVersione().setNome(nome);
            try
            {
                Dao<Versione> versioneDao = VersioneDao.getInstance();
                versioneDao.save(versioneAggregata.getVersione());
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        DatiVersione datiVersione = new DatiVersione();
        datiVersione.setVersione(codice);
        datiVersione.setDataDiUscita(dataDiUscita);
        datiVersione.setDescrizione(descrizione);
        datiVersione.setIva(iva);
        datiVersione.setPrezzo(prezzo);
        datiVersione.setQuantita(quantita);
        if(!datiVersione.equals(versioneAggregata.getDatiVersione()))
        {
            try
            {
                Dao<DatiVersione> datiVersioneDao = DatiVersioneDao.getInstance();
                datiVersioneDao.save(datiVersione);
            }
            catch(SQLException e)
            {
                req.setAttribute("SQLException", e);
                forward(req, res);
                return;
            }
        }

        File cartellaVersione = new File(sparkFiles, versioneAggregata.getVersione().getProdotto() + File.separator + codice);
        if(!cartellaVersione.exists())
        {
            if(!cartellaVersione.mkdirs())
                throw new ServletException("Impossibile creare la cartella della versione");
        }

        try
        {
            Dao<Immagine> immagineDao = ImmagineDao.getInstance();
            if(numImmagini < versioneAggregata.getImmagini().size())
            {
                for(int i = numImmagini; i < versioneAggregata.getImmagini().size(); i++)
                {
                    immagineDao.delete(versioneAggregata.getImmagini().get(i));
                    new File(cartellaVersione, i + ".jpg").delete();
                }
            }
            else if(numImmagini > versioneAggregata.getImmagini().size())
            {
                for(int i = versioneAggregata.getImmagini().size(); i < numImmagini; i++)
                {
                    String imagePath = "/" + versioneAggregata.getVersione().getProdotto() + "/" + codice + "/" + i + ".jpg";
                    Immagine immagine = new Immagine(codice, i, imagePath);
                    immagineDao.save(immagine);
                }
            }
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        if(!PartUtil.isEmpty(icona))
        {
            File iconaVersione = new File(cartellaVersione, "icona.jpg");
            icona.write(iconaVersione.getAbsolutePath());
        }

        for(int i = 0; i < numImmagini; i++)
        {
            if(!PartUtil.isEmpty(immagini.get(i)))
            {
                File immaginePath = new File(cartellaVersione, i + ".jpg");
                immagini.get(i).write(immaginePath.getAbsolutePath());
            }
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(prodottoSalvatoJsp);
        dispatcher.forward(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setAttribute("modifyVersion", Boolean.TRUE);

        String codice = req.getParameter("codice");

        VersioneAggregata versioneAggregata;
        try
        {
            versioneAggregata = VersioneAggregata.getVersioneAggregata(codice);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, resp);
            return;
        }

        req.setAttribute("versioneAggregata", versioneAggregata);
        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(insertVersionJsp);
        dispatcher.forward(req, res);
    }
}
