package losor.controller.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Cliente;
import losor.model.bean.Ordine;
import losor.model.dao.ClienteDao;
import losor.model.dao.OrdineDao;

@WebServlet(urlPatterns = {"/adminOrdini"},
            initParams = {@WebInitParam(name = "adminOrdiniJsp", value = "/jsp/adminOrdini.jsp")})
public class AdminOrdiniServlet extends HttpServlet
{
    private String adminOrdiniJsp;

    @Override
    public void init() throws ServletException
    {
        adminOrdiniJsp = getInitParameter("adminOrdiniJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String clienteParam = req.getParameter("cliente");
        String dataInizioParam = req.getParameter("dataInizio");
        String dataFineParam = req.getParameter("dataFine");

        List<Ordine> ordini;
        try
        {
            ordini = OrdineDao.getInstance().getAll(null);
        }
        catch (SQLException e)
        {
            throw new ServletException();
        }

        Stream<Ordine> stream = ordini.parallelStream().sorted(Comparator.comparing(Ordine::getNumFattura).reversed());

        if(!ParameterUtil.isEmpty(clienteParam))
        {
            Integer cliente = Integer.valueOf(clienteParam);
            stream = stream.filter(e -> e.getCliente().equals(cliente));
        }

        if(!ParameterUtil.isEmpty(dataInizioParam))
        {
            Date dataInizio = Date.valueOf(dataInizioParam);
            stream = stream.filter(e -> e.getData().compareTo(dataInizio) >= 0);
        }

        if(!ParameterUtil.isEmpty(dataFineParam))
        {
            Date dataFine = Date.valueOf(dataFineParam);
            stream = stream.filter(e -> e.getData().compareTo(dataFine) <= 0);
        }

        ordini = stream.toList();

        List<Cliente> clienti;
        try
        {
            clienti = ClienteDao.getInstance().getAll(null).parallelStream().sorted(Comparator.comparing(Cliente::getCodice)).toList();
        }
        catch (SQLException e)
        {
            throw new ServletException();
        }

        req.setAttribute("ordini", ordini);
        req.setAttribute("clienti", clienti);
        req.getRequestDispatcher(adminOrdiniJsp).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
