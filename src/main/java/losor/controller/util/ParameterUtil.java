package losor.controller.util;

public abstract class ParameterUtil
{
    public static boolean isEmpty(String parameter)
    {
        return parameter == null || parameter.isBlank();
    }

    public static boolean areEmpty(String... parameters)
    {
        for(int i = 0; i < parameters.length; i++)
        {
            if(!isEmpty(parameters[i]))
                return false;
        }
        return true;
    }
}