package losor.model.util;

import javax.servlet.http.HttpSession;

public abstract class SessionUtil
{
    public static Object getSessionLock(HttpSession session)
    {
        return session.getId().intern();
    }
}
