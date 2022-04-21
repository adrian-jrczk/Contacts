package contacts.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Locale;

public class PhoneNumberValidator {

    static private final Locale LOCALE = Locale.getDefault();
    static private final PhoneNumberUtil NUMBER_UTIL = PhoneNumberUtil.getInstance();

    static public boolean isValid(String numberToValidate) {
        try {
            Phonenumber.PhoneNumber phoneNumber = NUMBER_UTIL.parse(numberToValidate, LOCALE.getCountry());
            return NUMBER_UTIL.isValidNumber(phoneNumber);
        } catch (NumberParseException exception) {
            return false;
        }
    }

}
