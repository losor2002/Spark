package losor.controller.util;

import javax.servlet.http.Part;

public abstract class PartUtil
{
    public static boolean isEmpty(Part part)
    {
        return part == null || part.getSize() == 0;
    }

    public static boolean areEmpty(Part... parts)
    {
        for(int i = 0; i < parts.length; i++)
        {
            if(!isEmpty(parts[i]))
                return false;
        }
        return true;
    }
}
