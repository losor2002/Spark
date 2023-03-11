package losor.model.util;

import java.util.Optional;

import javax.servlet.http.HttpSession;

public abstract class UserUtil
{
    public static Optional<Integer> getUserId(HttpSession session)
    {
        if(session == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            return Optional.ofNullable((Integer) session.getAttribute("userId"));
        }
    }

    public static void setUserId(HttpSession session, Integer userId)
    {
        if(session == null || userId == null)
            throw new NullPointerException();

        synchronized(SessionUtil.getSessionLock(session))
        {
            session.setAttribute("userId", userId);
        }
    }

    public static boolean isUserAuth(HttpSession session)
    {
        return getUserId(session).isPresent();
    }

    public static void logout(HttpSession session)
    {
        if(session == null)
            throw new NullPointerException();
            
        synchronized(SessionUtil.getSessionLock(session))
        {
            session.removeAttribute("userId");
        }
    }

    public static Object getUserLock(HttpSession session)
    {
        return getUserId(session).get().toString().intern();
    }
}