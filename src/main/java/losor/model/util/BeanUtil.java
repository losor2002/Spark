package losor.model.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import losor.model.bean.Bean;

public abstract class BeanUtil
{
    private static Method getGetter(Field field)
    {
        Class<?> classe = field.getDeclaringClass();
        char[] nameArray = field.getName().toCharArray();
        nameArray[0] = Character.toUpperCase(nameArray[0]);
        String name = "get" + new String(nameArray);
        Method method;
        try
        {
            method = classe.getDeclaredMethod(name);
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException("Il field " + field.getName() + " del bean " + classe.getSimpleName() + " non ha il getter");
        }
        return method;
    }

    private static Method getSetter(Field field)
    {
        Class<?> classe = field.getDeclaringClass();
        char[] nameArray = field.getName().toCharArray();
        nameArray[0] = Character.toUpperCase(nameArray[0]);
        String name = "set" + new String(nameArray);
        Method method;
        try
        {
            method = classe.getDeclaredMethod(name, field.getType());
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException("Il field " + field.getName() + " del bean " + classe.getSimpleName() + " non ha il setter");
        }
        return method;
    }

    public static <T extends Bean> Object invokeGetter(T obj, Field field)
    {
        if(obj == null || field == null)
            throw new NullPointerException();

        try
        {
            return getGetter(field).invoke(obj);
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException("Il getter del field " + field.getName() + " della classe " + field.getDeclaringClass().getSimpleName() + " non è pubblico");
        }
        catch(InvocationTargetException e)
        {
            throw new RuntimeException("Il getter del field " + field.getName() + " della classe " + field.getDeclaringClass().getSimpleName() + " ha generato un'eccezione: " + e.getCause());
        }
    }

    public static <T extends Bean> void invokeSetter(T obj, Field field, Object value)
    {
        if(obj == null || field == null)
            throw new NullPointerException();

        try
        {
            getSetter(field).invoke(obj, value);
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException("Il setter del field " + field.getName() + " della classe " + field.getDeclaringClass().getSimpleName() + " non è pubblico");
        }
        catch(InvocationTargetException e)
        {
            throw new RuntimeException("Il setter del field " + field.getName() + " della classe " + field.getDeclaringClass().getSimpleName() + " ha generato un'eccezione: " + e.getCause());
        }
    }

    public static <T extends Bean> T newInstance(Class<T> beanClass)
    {
        if(beanClass == null)
            throw new NullPointerException();
            
        try
        {
            return beanClass.getDeclaredConstructor().newInstance();
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException("Il bean " + beanClass.getSimpleName() + " non ha un costruttore a 0 argomenti");
        }
        catch(InstantiationException e)
        {
            throw new RuntimeException("Il bean " + beanClass.getSimpleName() + " è astratto");
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException("Il costruttore  del bean " + beanClass.getSimpleName() + " non è pubblico");
        }
        catch(InvocationTargetException e)
        {
            throw new RuntimeException("Il costruttore del bean " + beanClass.getSimpleName() + " ha generato un'eccezione: " + e.getCause());
        }
    }
}