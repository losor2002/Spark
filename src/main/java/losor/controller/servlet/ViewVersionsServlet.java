package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.util.VersioneAggregata;

@WebServlet(urlPatterns = {"/viewVersions"},
            initParams = {@WebInitParam(name = "viewVersionsJsp", value = "/jsp/viewVersions.jsp")})
public class ViewVersionsServlet extends HttpServlet
{
    private String viewVersionsJsp;

    @Override
    public void init() throws ServletException
    {
        viewVersionsJsp = getInitParameter("viewVersionsJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String prodottoParam = req.getParameter("prodotto");
        Integer prodotto = Integer.valueOf(prodottoParam);

        List<VersioneAggregata> versioniAggregate;
        try
        {
            versioniAggregate = VersioneAggregata.getVersioniAggregate(prodotto);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        req.setAttribute("versioniAggregate", versioniAggregate);
        forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewVersionsJsp);
        dispatcher.forward(req, res);
    }
}
