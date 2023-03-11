package losor.controller.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.model.util.AdminUtil;
import losor.model.util.SessionUtil;

@WebFilter(urlPatterns = {"/adminLogin"},
            initParams = {@WebInitParam(name = "adminHome", value = "/adminHome")})
public class AdminLoggedFilter extends HttpFilter
{
    private String adminHome;

    @Override
    public void init() throws ServletException
    {
        adminHome = getServletContext().getContextPath() + getInitParameter("adminHome");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpSession session = req.getSession();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(!AdminUtil.isAdminAuth(session))
                chain.doFilter(req, res);
            else
                res.sendRedirect(adminHome);
        }
    }
}