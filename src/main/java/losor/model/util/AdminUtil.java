package losor.model.util;

import java.util.Optional;

import javax.servlet.http.HttpSession;

public abstract class AdminUtil
{
    public static Optional<Integer> getAdminId(HttpSession session)
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            return Optional.ofNullable((Integer) session.getAttribute("adminId"));
        }
    }

    public static void setAdminId(HttpSession session, Integer adminId)
    {
        if(session == null || adminId == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            session.setAttribute("adminId", adminId);
        }
    }

    public static boolean isAdminAuth(HttpSession session)
    {
        return getAdminId(session).isPresent();
    }

    public static void logout(HttpSession session)
    {
        if(session == null)
            throw new NullPointerException();
            
        synchronized(SessionUtil.getSessionLock(session))
        {
            session.removeAttribute("adminId");
        }
    }
}