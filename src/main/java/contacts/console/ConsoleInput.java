package contacts.console;

import contacts.utils.EmailValidator;
import contacts.utils.PhoneNumberValidator;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    static String getName() {
        while (true) {
            System.out.print("name > ");
            String name = SCANNER.nextLine().trim();
            if (name.isBlank()) {
                System.out.println("Name field cannot be empty");
            } else if (name.length() < 5) {
                System.out.println("Name length is too short, try again");
            } else {
                return name;
            }
        }
    }

    static String getNumber() {
        while (true) {
            System.out.print("number > ");
            String numberString = SCANNER.nextLine().trim();
            if (numberString.isBlank()) {
                System.out.println("Phone number field cannot be empty");
            } else if (!PhoneNumberValidator.isValid(numberString)) {
                System.out.println("Phone number has incorrect format");
            } else {
                return numberString;
            }
        }
    }

    static String getType() {
        System.out.print("type[home, fax, etc.] > ");
        return SCANNER.nextLine().trim();
    }

    static String getEmail() {
        while (true) {
            System.out.print("email > ");
            String emailString = SCANNER.nextLine();
            if (emailString.isBlank()) {
                return "";
            }
            if (!EmailValidator.isValid(emailString)) {
                System.out.println("Incorrect email format, try again");
            } else {
                return emailString;
            }
        }
    }

    static String getNote() {
        System.out.print("note > ");
        String note = SCANNER.nextLine();
        if (note.isBlank()) {
            return "";
        }
        return note;
    }

    static String getStreet() {
        System.out.print("street > ");
        return SCANNER.nextLine();
    }

    static String getCity() {
        System.out.print("city > ");
        return SCANNER.nextLine();
    }

    static String getCountry() {
        System.out.print("country > ");
        return SCANNER.nextLine();
    }

    static String getPostalCode() {
        System.out.print("postal code > ");
        return SCANNER.nextLine();
    }

    static LocalDate getBirthDate() {
        while (true) {
            System.out.print("birth date[yyyy-mm-dd] > ");
            try {
                String input = SCANNER.nextLine();
                if (input.isBlank()) {
                    return null;
                }
                return LocalDate.parse(input);
            } catch (IllegalArgumentException exception) {
                System.out.println("Incorrect input. Try again");
            }
        }
    }

    static String getLine() {
        System.out.print(" > ");
        return SCANNER.next() + SCANNER.nextLine();
    }

    static boolean getDeletionConfirmation() {
        while (true) {
            System.out.print("Are you sure you want to delete this contact? [y|n] > ");
            String answer = SCANNER.next() + SCANNER.nextLine();
            if (answer.equals("y") || answer.equals("Y")) {
                return true;
            } else if (answer.equals("n") || answer.equals("N")) {
                return false;
            } else {
                System.out.println("Incorrect answer");
            }
        }

    }
}
