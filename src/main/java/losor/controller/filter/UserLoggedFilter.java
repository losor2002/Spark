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

import losor.model.util.SessionUtil;
import losor.model.util.UserUtil;

@WebFilter(urlPatterns = {"/userLogin", "/userSignUp"},
            initParams = {@WebInitParam(name = "home", value = "/home")})
public class UserLoggedFilter extends HttpFilter
{
    private String home;

    @Override
    public void init() throws ServletException
    {
        home = getServletContext().getContextPath() + getInitParameter("home");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpSession session = req.getSession();

        synchronized(SessionUtil.getSessionLock(session))
        {
            if(!UserUtil.isUserAuth(session))
                chain.doFilter(req, res);
            else
                res.sendRedirect(home);
        }
    }
}