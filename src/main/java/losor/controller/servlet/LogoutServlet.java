package losor.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import losor.controller.util.ParameterUtil;
import losor.model.util.AdminUtil;
import losor.model.util.UserUtil;

@WebServlet(urlPatterns = {"/logout"},
            initParams = {@WebInitParam(name = "home", value = "/home")})
public class LogoutServlet extends HttpServlet
{
    private String home;

    @Override
    public void init() throws ServletException
    {
        home = getServletContext().getContextPath() + getInitParameter("home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String who = req.getParameter("who");

        if(ParameterUtil.isEmpty(who))
        {
            resp.sendRedirect(home);
            return;
        }

        HttpSession session = req.getSession(false);

        if(session == null)
        {
            resp.sendRedirect(home);
            return;
        }

        if(who.equals("user"))
            UserUtil.logout(session);
        else if(who.equals("admin"))
            AdminUtil.logout(session);
        
        resp.sendRedirect(home);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}