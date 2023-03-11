package losor.model.util;

import java.util.regex.Pattern;

public class EmailValidator
{
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private EmailValidator() {}

    public static boolean validate(String email)
    {
        return pattern.matcher(email).matches();
    }
}