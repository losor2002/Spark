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

import losor.model.util.AdminUtil;
import losor.model.util.SessionUtil;

@WebFilter(urlPatterns = {"/adminHome", "/jsp/adminHome.jsp", "/insertProduct", "/jsp/insertProduct.jsp", "/jsp/prodottoSalvato.jsp", "/modifyProduct", "/insertVersion", "/jsp/insertVersion.jsp",
                          "/viewVersions", "/jsp/viewVersions.jsp", "/modifyVersion", "/deleteVersion", "/deleteProduct", "/adminOrdini", "/jsp/adminOrdini.jsp", "/dettagliOrdineAdmin",
                          "/utenti", "/jsp/utenti.jsp"},
            initParams = {@WebInitParam(name = "adminLoginServlet", value = "/adminLogin")})
public class AdminLoginFilter extends HttpFilter
{
    private String adminLoginServlet;

    @Override
    public void init() throws ServletException
    {
        adminLoginServlet = getInitParameter("adminLoginServlet");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpSession session = req.getSession();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(AdminUtil.isAdminAuth(session))
                chain.doFilter(req, res);
            else
            {
                req.setAttribute("filteredResource", req.getRequestURL().toString());
                RequestDispatcher dispatcher = req.getRequestDispatcher(adminLoginServlet);
                dispatcher.forward(req, res);
            }
        }
    }
}