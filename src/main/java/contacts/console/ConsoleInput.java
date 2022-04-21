package contacts.console;

import contacts.utils.EmailValidator;
import contacts.utils.PhoneNumberValidator;
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
            String number = SCANNER.nextLine().trim();
            if (number.isBlank()) {
                System.out.println("Phone number field cannot be empty");
            } else if (!PhoneNumberValidator.isValid(number)) {
                System.out.println("Phone number has incorrect format");
            } else {
                return number;
            }
        }
    }

    static String getEmail() {
        while (true) {
            System.out.print("email > ");
            String email = SCANNER.nextLine();
            if (email.isBlank()) {
                return "";
            }
            if (!EmailValidator.isValid(email)) {
                System.out.println("Incorrect email format, try again");
            } else {
                return email;
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

    static String getAddress() {
        System.out.print("address > ");
        return SCANNER.nextLine();
    }

    static String getBirthDate() {
        while (true) {
            System.out.print("birth date[yyyy-mm-dd] > ");
            try {
                String input = SCANNER.nextLine();
                if (input.isBlank()) {
                    return "";
                }
                java.sql.Date.valueOf(input);
                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println("Incorrect input. Try again");
            }
        }
    }

    static String getInstructions() {
        System.out.print(" > ");
        return SCANNER.next() + SCANNER.nextLine();
    }
}
