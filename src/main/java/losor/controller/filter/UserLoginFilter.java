package losor.controller.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.model.util.SessionUtil;
import losor.model.util.UserUtil;

@WebFilter(urlPatterns = {"/ordine", "/jsp/ordine.jsp", "/jsp/orderSuccess.jsp", "/insertIndirizzo", "/insertCarta", "/areaPersonale", "/jsp/areaPersonale.jsp", "/modificaProfilo",
                        "/areaPersonale/indirizzi", "/jsp/indirizzi.jsp", "/deleteIndirizzo", "/areaPersonale/carte", "/jsp/carte.jsp", "/deleteCarta", "/areaPersonale/ordini",
                        "/jsp/ordini.jsp", "/dettagliOrdine", "/fattura", "/jsp/fattura.jsp"},
            initParams = {@WebInitParam(name = "userLoginServlet", value = "/userLogin")})
public class UserLoginFilter extends HttpFilter
{
    private String userLoginServlet;

    @Override
    public void init() throws ServletException
    {
        userLoginServlet = getInitParameter("userLoginServlet");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpSession session = req.getSession();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(UserUtil.isUserAuth(session))
                chain.doFilter(req, res);
            else
            {
                req.setAttribute("filteredResource", req.getRequestURL().toString());
                RequestDispatcher dispatcher = req.getRequestDispatcher(userLoginServlet);
                dispatcher.forward(req, res);
            }
        }
    }
}