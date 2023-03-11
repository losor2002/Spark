package losor.model.util;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

public class PwEncryptor
{
    private final static PasswordEncryptor instance = new BasicPasswordEncryptor();

    private PwEncryptor() {}

    public static String encryptPassword(String password)
    {
        return instance.encryptPassword(password);
    }

    public static boolean checkPassword(String password, String encryptedPassword)
    {
        return instance.checkPassword(password, encryptedPassword);
    }
}