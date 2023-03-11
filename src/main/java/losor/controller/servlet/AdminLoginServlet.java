package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Amministratore;
import losor.model.dao.AmministratoreDao;
import losor.model.dao.Dao;
import losor.model.util.AdminUtil;
import losor.model.util.EmailValidator;
import losor.model.util.PwEncryptor;
import losor.model.util.PwValidator;

@WebServlet(initParams = {@WebInitParam(name = "loginJsp", value = "/jsp/login.jsp")},
            urlPatterns = {"/adminLogin"})
public class AdminLoginServlet extends HttpServlet
{
    private String loginJsp;

    @Override
    public void init() throws ServletException
    {
        loginJsp = getInitParameter("loginJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        if(req.getAttribute("filteredResource") == null)
            req.setAttribute("filteredResource", req.getRequestURL().toString());

        req.setAttribute("adminLogin", Boolean.TRUE);

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if(ParameterUtil.areEmpty(email, password))
        {
            forward(req, res);
            return;
        }

        if(ParameterUtil.isEmpty(email))
        {
            req.setAttribute("emailVuota", Boolean.TRUE);
            forward(req, res);
            return;
        }
        if(ParameterUtil.isEmpty(password))
        {
            req.setAttribute("passwordVuota", Boolean.TRUE);
            forward(req, res);
            return;
        }

        if(!EmailValidator.validate(email))
        {
            req.setAttribute("invalidEmail", Boolean.TRUE);
            forward(req, res);
            return;
        }
        if(!PwValidator.validate(password))
        {
            req.setAttribute("invalidPassword", Boolean.TRUE);
            forward(req, res);
            return;
        }
        
        Dao<Amministratore> dao = AmministratoreDao.getInstance();
        Amministratore admin = new Amministratore();
        admin.setEmail(email);
        Optional<Amministratore> optional = null;

        try
        {
            optional = dao.get(admin);
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

        if(optional.isEmpty())
        {
            req.setAttribute("emailNotPresent", Boolean.TRUE);
            forward(req, res);
            return;
        }

        admin = optional.get();
        if(PwEncryptor.checkPassword(password, admin.getPassword()))
        {
            AdminUtil.setAdminId(req.getSession(), admin.getCodice());
            String filteredResource = (String) req.getAttribute("filteredResource");
            res.sendRedirect(filteredResource);
            return;
        }
        else
        {
            req.setAttribute("wrongPassword", Boolean.TRUE);
            forward(req, res);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(loginJsp);
        dispatcher.forward(req, res);
    }
}