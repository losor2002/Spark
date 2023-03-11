package losor.controller.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.model.util.ProdottiByCategoria;

@WebServlet(urlPatterns = {"/home"},
            initParams = {@WebInitParam(name = "homeJsp", value = "/jsp/home.jsp")})
public class HomeServlet extends HttpServlet
{
    private String homeJsp;

    @Override
    public void init() throws ServletException
    {
        homeJsp = getInitParameter("homeJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        req.setAttribute("prodottiByCategoria", ProdottiByCategoria.getInstance());
        
        forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(homeJsp);
        dispatcher.forward(req, res);
    }
}