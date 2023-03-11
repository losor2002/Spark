package losor.controller.servlet;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.lowagie.text.DocumentException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import losor.controller.util.CharArrayWriterResponse;
import losor.controller.util.ParameterUtil;
import losor.model.bean.Composizione;
import losor.model.bean.Indirizzo;
import losor.model.bean.Ordine;
import losor.model.bean.Prodotto;
import losor.model.bean.Versione;
import losor.model.dao.ComposizioneDao;
import losor.model.dao.Dao;
import losor.model.dao.IndirizzoDao;
import losor.model.dao.OrdineDao;
import losor.model.dao.ProdottoDao;
import losor.model.dao.VersioneDao;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/fattura"},
            initParams = {@WebInitParam(name = "fatturaJsp", value = "/jsp/fattura.jsp")})
public class FatturaServlet extends HttpServlet
{
    private String fatturaJsp;

    @Override
    public void init() throws ServletException
    {
        fatturaJsp = getInitParameter("fatturaJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String numFatturaParam = req.getParameter("numFattura");

        if(ParameterUtil.isEmpty(numFatturaParam))
            throw new ServletException();

        Integer numFattura = Integer.valueOf(numFatturaParam);

        try
        {
            Ordine ordine = new Ordine();
            ordine.setNumFattura(numFattura);
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
        
        CharArrayWriterResponse customResponse = new CharArrayWriterResponse(resp);
        req.getRequestDispatcher(fatturaJsp).forward(req, customResponse);
        String html = customResponse.getOutput();

        try
        {
            Reader reader = new CharArrayReader(html.toCharArray());
            InputSource inputSource = new InputSource(reader);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(inputSource);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(doc, null);
		    renderer.layout();

            OutputStream out = resp.getOutputStream();
		    resp.setContentType("application/pdf");
		    //resp.setHeader("Content-disposition", "attachment; filename=export.pdf");
		    //resp.setContentLength(data.length);
            
		    renderer.createPDF(out);
        }
        catch(ParserConfigurationException | SAXException | DocumentException e)
        {
            if(e instanceof ParserConfigurationException)
                throw new ServletException("ParserConfigurationException");
            else if(e instanceof SAXException)
                throw new ServletException(e.getMessage());
            else
                throw new ServletException("DocumentException");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
