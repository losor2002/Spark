package losor.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import losor.controller.util.ParameterUtil;
import losor.controller.util.ResponseUtil;
import losor.model.bean.Cliente;
import losor.model.dao.ClienteDao;
import losor.model.util.EmailValidator;
import losor.model.util.PwEncryptor;
import losor.model.util.PwValidator;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/modificaProfilo"})
public class ModificaProfiloServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String toChange = req.getParameter("toChange");

        if(ParameterUtil.isEmpty(toChange))
            throw new ServletException();

        Cliente cliente;

        switch(toChange)
        {
            case "nome":
            case "cognome":
            case "email":
                String value = req.getParameter("value");

                if(ParameterUtil.isEmpty(value))
                    throw new ServletException();
                
                cliente = new Cliente();
                cliente.setCodice(UserUtil.getUserId(req.getSession()).get());

                try
                {
                    cliente = ClienteDao.getInstance().get(cliente).get();

                    String valoreAttuale = null;
                    
                    if(toChange.equals("nome"))
                        valoreAttuale = cliente.getNome();
                    else if(toChange.equals("cognome"))
                        valoreAttuale = cliente.getCognome();
                    else if(toChange.equals("email"))
                        valoreAttuale = cliente.getEmail();

                    if(value.equals(valoreAttuale))
                    {
                        ResponseUtil.writeJsonResponse(resp, "{\"value\":\"" + valoreAttuale + "\"}");
                        return;
                    }

                    if(toChange.equals("nome"))
                        cliente.setNome(value);
                    else if(toChange.equals("cognome"))
                        cliente.setCognome(value);
                    else if(toChange.equals("email"))
                    {
                        if(!EmailValidator.validate(value))
                        {
                            ResponseUtil.writeJsonErrorResponse(resp, "Formato dell'email errato");
                            return;
                        }

                        Cliente othCliente = new Cliente();
                        othCliente.setEmail(value);

                        if(ClienteDao.getInstance().exists(othCliente))
                        {
                            ResponseUtil.writeJsonErrorResponse(resp, "Email già in uso da un altro account");
                            return;
                        }

                        cliente.setEmail(value);
                    }

                    ClienteDao.getInstance().save(cliente);
                }
                catch(SQLException e)
                {
                    throw new ServletException();
                }

                ResponseUtil.writeJsonResponse(resp, "{\"value\":\"" + value + "\"}");
                break;

            case "password":
                String passwordAttuale = req.getParameter("passwordAttuale");
                String passwordNuova = req.getParameter("passwordNuova");

                if(ParameterUtil.areEmpty(passwordAttuale, passwordNuova))
                    throw new ServletException();

                if(!PwValidator.validate(passwordAttuale) || !PwValidator.validate(passwordNuova))
                {
                    ResponseUtil.writeJsonErrorResponse(resp, "Formato della password errato");
                    return;
                }

                cliente = new Cliente();
                cliente.setCodice(UserUtil.getUserId(req.getSession()).get());

                try
                {
                    cliente = ClienteDao.getInstance().get(cliente).get();

                    if(!PwEncryptor.checkPassword(passwordAttuale, cliente.getPassword()))
                    {
                        ResponseUtil.writeJsonErrorResponse(resp, "Password attuale non corretta");
                        return;
                    }

                    cliente.setPassword(PwEncryptor.encryptPassword(passwordNuova));

                    ClienteDao.getInstance().save(cliente);
                }
                catch(SQLException e)
                {
                    throw new ServletException();
                }

                ResponseUtil.writeJsonResponse(resp, "{}");
                break;

            default:
                throw new ServletException();
        }
    }
}
