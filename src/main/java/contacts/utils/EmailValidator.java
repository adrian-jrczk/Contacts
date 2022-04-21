package contacts.utils;

public class EmailValidator {

    static public final String RFC_5322_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean isValid(String email) {
        return email.matches(RFC_5322_REGEX);
    }
}
