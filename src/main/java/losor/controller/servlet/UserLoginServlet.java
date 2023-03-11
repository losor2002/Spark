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
import javax.servlet.http.HttpSession;

import losor.controller.util.ParameterUtil;
import losor.model.bean.Cliente;
import losor.model.dao.ClienteDao;
import losor.model.dao.Dao;
import losor.model.util.CarrelloUtil;
import losor.model.util.EmailValidator;
import losor.model.util.PwEncryptor;
import losor.model.util.PwValidator;
import losor.model.util.SessionUtil;
import losor.model.util.UserUtil;

@WebServlet(initParams = {@WebInitParam(name = "loginJsp", value = "/jsp/login.jsp")},
            urlPatterns = {"/userLogin"})
public class UserLoginServlet extends HttpServlet
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
        
        Dao<Cliente> dao = ClienteDao.getInstance();
        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        Optional<Cliente> optional = null;

        try
        {
            optional = dao.get(cliente);
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

        cliente = optional.get();
        if(PwEncryptor.checkPassword(password, cliente.getPassword()))
        {
            HttpSession session = req.getSession();

            synchronized(SessionUtil.getSessionLock(session))
            {
                UserUtil.setUserId(session, cliente.getCodice());
                
                try
                {
                    CarrelloUtil.transferToDB(session);
                }
                catch(SQLException e)
                {
                    UserUtil.logout(session);
                    req.setAttribute("SQLException", e);
                    forward(req, res);
                    return;
                }
            }

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