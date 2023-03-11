package losor.controller.util;

import java.util.stream.Collectors;

public abstract class TextUtil
{
    public static String htmlEncode(String string)
    {
        return string.lines().collect(Collectors.joining("<br>"));
    }

    public static String idEncode(String string)
    {
        return string.replaceAll("\s", "_");
    }
}
