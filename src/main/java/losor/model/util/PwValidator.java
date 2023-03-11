package losor.model.util;

import java.util.regex.Pattern;

public class PwValidator
{
    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]{8,}$");

    private PwValidator() {}

    public static boolean validate(String password)
    {
        return pattern.matcher(password).matches();
    }
}