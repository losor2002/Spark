package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

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

@WebServlet(urlPatterns = {"/userSignUp"},
            initParams = {@WebInitParam(name = "signUpJsp", value = "/jsp/signUp.jsp"),
                          @WebInitParam(name = "successJsp", value = "/jsp/registrationSuccess.jsp")})
public class UserSignUpServlet extends HttpServlet
{
    private String signUpJsp, successJsp;

    @Override
    public void init() throws ServletException
    {
        signUpJsp = getInitParameter("signUpJsp");
        successJsp = getInitParameter("successJsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if(ParameterUtil.areEmpty(nome, cognome, email, password))
        {
            forward(req, res);
            return;
        }

        if(ParameterUtil.isEmpty(nome))
        {
            req.setAttribute("nomeVuoto", Boolean.TRUE);
            forward(req, res);
            return;
        }
        if(ParameterUtil.isEmpty(cognome))
        {
            req.setAttribute("cognomeVuoto", Boolean.TRUE);
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
            req.setAttribute("emailNonValida", Boolean.TRUE);
            forward(req, res);
            return;
        }
        if(!PwValidator.validate(password))
        {
            req.setAttribute("passwordNonValida", Boolean.TRUE);
            forward(req, res);
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        Dao<Cliente> dao = ClienteDao.getInstance();

        try
        {
            if(dao.exists(cliente))
            {
                req.setAttribute("emailGiàRegistrata", Boolean.TRUE);
                forward(req, res);
                return;
            }

            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setPassword(PwEncryptor.encryptPassword(password));
            cliente.setNumAcquisti(0);
            dao.save(cliente);

            cliente = dao.get(cliente).get();
        }
        catch(SQLException e)
        {
            req.setAttribute("SQLException", e);
            forward(req, res);
            return;
        }

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
                CarrelloUtil.emptySessionCart(session);
            }
        }
        
        RequestDispatcher dispatcher = req.getRequestDispatcher(successJsp);
        dispatcher.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = req.getRequestDispatcher(signUpJsp);
        dispatcher.forward(req, res);
    }
}