package contacts.console;

import contacts.database.entity.Address;
import contacts.database.entity.Contact;
import contacts.database.Database;
import contacts.database.DatabaseOperationException;
import contacts.database.entity.Email;
import contacts.database.entity.PhoneNumber;
import contacts.utils.FileOperationException;
import contacts.utils.FileUtils;
import java.util.List;

public class ConsoleInterface {

    private final Database DATABASE = new Database();

    public void open() {
        while (true) {
            try {
                String inputLine = ConsoleInput.getLine();
                switch (InputRecognizer.recognize(inputLine)) {
                    case ADD_CONTACT -> addContact();
                    case GET_ALL -> getAll();
                    case ACCESS -> access(inputLine);
                    case SEARCH -> searchByPattern(inputLine);
                    case IMPORT -> importFromFile(inputLine);
                    case HELP -> printHelpMessage();
                    case INCORRECT -> System.out.println("Incorrect input");
                    case EXIT -> {
                        DATABASE.closeEntityManagerFactory();
                        return;
                    }
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void addContact() throws DatabaseOperationException {
        Contact newContact = new Contact();

        newContact.setName(ConsoleInput.getName());

        PhoneNumber number = new PhoneNumber();
        number.setNumber(ConsoleInput.getNumber());
        number.setType(ConsoleInput.getType());
        newContact.addNumber(number);

        Email email = new Email();
        email.setEmail(ConsoleInput.getEmail());
        newContact.addEmail(email);

        Address address = new Address();
        address.setStreet(ConsoleInput.getStreet());
        address.setCity(ConsoleInput.getCity());
        address.setCountry(ConsoleInput.getCountry());
        address.setPostalCode(ConsoleInput.getPostalCode());
        newContact.setAddress(address);

        newContact.setBirthDate(ConsoleInput.getBirthDate());
        newContact.setNote(ConsoleInput.getNote());
        DATABASE.saveContact(newContact);
        System.out.println(newContact.getName() + " was successfully saved in the database");
    }

    private void getAll() throws DatabaseOperationException {
        List<Contact> contacts = DATABASE.getAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts saved");
        } else {
            printContactList(contacts);
        }
    }

    private void access(String input) throws DatabaseOperationException {
        String name = input.substring(6).trim();
        Contact contact = DATABASE.getByName(name);
        System.out.println(contact);
        while (true) {
            System.out.print("[contact]");
            String inputLine = ConsoleInput.getLine().toLowerCase();
            switch (InputRecognizer.recognize(inputLine)) {
                case ADD_CONTACT_ITEM:
                    String itemName = inputLine.split("\\s+")[1];
                    switch (itemName) {
                        case "number" -> addNumber(contact);
                        case "email" -> addEmail(contact);
                        default -> System.out.println("Incorrect item");
                    }
                    DATABASE.updateContact(contact);
                    System.out.println(contact);
                    break;
                case EDIT_FIELD:
                    String fieldToEdit = inputLine.split("\\s+")[1];
                    switch (fieldToEdit) {
                        case "name" -> editName(contact);
                        case "number" -> editNumber(contact, inputLine);
                        case "email" -> editEmail(contact, inputLine);
                        case "address" -> editAddress(contact, inputLine);
                        case "birth date" -> contact.setBirthDate(ConsoleInput.getBirthDate());
                        case "note" -> contact.setNote(ConsoleInput.getNote());
                    }
                    DATABASE.updateContact(contact);
                    System.out.println(contact);
                    break;
                case DELETE_CONTACT:
                    boolean delete = ConsoleInput.getDeletionConfirmation();
                    if (delete) {
                        DATABASE.deleteContact(contact);
                        System.out.println("Contact deleted");
                        return;
                    }
                    break;
                case DELETE_FIELD:
                    deleteField(contact, inputLine);
                    DATABASE.updateContact(contact);
                    System.out.println(contact);
                    break;
                case RETURN:
                    return;
                default:
                    System.out.println("""
                            Incorrect input.
                            You can use following commands:
                            edit CONTACT_FIELD ITEM_NUMBER ITEM_FIELD
                            edit CONTACT_FIELD ITEM_FIELD
                            add number
                            add email
                            delete CONTACT_FIELD ITEM_NUMBER
                            delete CONTACT_FIELD
                            return
                                               
                            """);
            }
        }
    }

    private void addNumber(Contact contact) {
        PhoneNumber number = new PhoneNumber();
        number.setNumber(ConsoleInput.getNumber());
        number.setType(ConsoleInput.getType());
        contact.addNumber(number);
    }

    private void addEmail(Contact contact) {
        Email email = new Email();
        email.setEmail(ConsoleInput.getEmail());
        contact.addEmail(email);
    }

    private void editName(Contact contact) {
        contact.setName(ConsoleInput.getName());
    }

    private void editNumber(Contact contact, String userInput) {
        String[] instructions = userInput.split("\\s+");
        if (contact.getNumbers().size() == 1) {
            if (instructions.length != 3) {
                System.out.println("Incorrect instructions");
                return;
            }
            PhoneNumber number = contact.getNumbers().get(0);
            String field = instructions[2];
            if (field.equals("number")) {
                number.setNumber(ConsoleInput.getNumber());
            } else if (field.equals("type")) {
                number.setType(ConsoleInput.getType());
            } else {
                System.out.println("Incorrect field");
            }
        } else {
            if (instructions.length != 4) {
                System.out.println("Incorrect instructions");
                return;
            }
            if (!instructions[2].matches("\\d+")) {
                System.out.println("Incorrect instructions");
                return;
            }
            List<PhoneNumber> numbers = contact.getNumbers();
            int numberIndex = Integer.parseInt(instructions[2]);
            if (numberIndex < 1 || numberIndex > numbers.size()) {
                System.out.println("Incorrect instructions");
                return;
            }
            PhoneNumber number = numbers.get(numberIndex - 1);
            String field = instructions[3];
            if (field.equals("number")) {
                number.setNumber(ConsoleInput.getNumber());
            } else if (field.equals("type")) {
                number.setType(ConsoleInput.getType());
            } else {
                System.out.println("Incorrect field");
            }
        }
    }

    private void editEmail(Contact contact, String userInput) {
        String[] instructions = userInput.split("\\s+");
        if (contact.getEmails().size() == 1) {
            if (instructions.length != 2) {
                System.out.println("Incorrect instructions");
                return;
            }
            Email email = contact.getEmails().get(0);
            email.setEmail(ConsoleInput.getEmail());
        } else {
            if (instructions.length != 3) {
                System.out.println("Incorrect instructions");
                return;
            }
            if (!instructions[2].matches("\\d+")) {
                System.out.println("Incorrect instructions");
                return;
            }
            List<Email> emails = contact.getEmails();
            int numberIndex = Integer.parseInt(instructions[2]);
            if (numberIndex < 1 || numberIndex > emails.size()) {
                System.out.println("Incorrect instructions");
                return;
            }
            Email email = emails.get(numberIndex - 1);
            email.setEmail(ConsoleInput.getEmail());
        }
    }

    private void editAddress(Contact contact, String userInput) {
        String[] instructions = userInput.split("\\s+");
        if (instructions.length != 3) {
            System.out.println("Incorrect instructions");
            return;
        }
        Address address = contact.getAddress();
        String field = instructions[2].toLowerCase();
        switch (field) {
            case "street" -> address.setStreet(ConsoleInput.getStreet());
            case "city" -> address.setCity(ConsoleInput.getCity());
            case "country" -> address.setCountry(ConsoleInput.getCountry());
            case "postal-code" -> address.setPostalCode(ConsoleInput.getPostalCode());
            default -> System.out.println("Incorrect field");
        }
    }

    private void deleteField(Contact contact, String userInput) {
        String[] instructions = userInput.split("\\s+");
        String fieldName = instructions[1];
        switch (fieldName) {
            case "number" -> {
                List<PhoneNumber> numbers = contact.getNumbers();
                if (numbers.size() == 1) {
                    contact.removeNumber(numbers.get(0));
                } else if (numbers.size() > 1) {
                    if (instructions.length != 3) {
                        System.out.println("Incorrect instructions");
                        return;
                    }
                    int numberIndex = Integer.parseInt(instructions[2]);
                    if (numberIndex > 0 && numberIndex <= numbers.size()) {
                        PhoneNumber number = numbers.get(numberIndex);
                        contact.removeNumber(number);
                    } else {
                        System.out.println("Incorrect element index");
                    }
                }
            }
            case "email" -> {
                List<Email> emails = contact.getEmails();
                if (emails.size() == 1) {
                    contact.removeEmail(emails.get(0));
                } else if (emails.size() > 1) {
                    if (instructions.length != 3) {
                        System.out.println("Incorrect instructions");
                        return;
                    }
                    int emailIndex = Integer.parseInt(instructions[2]);
                    if (emailIndex > 0 && emailIndex <= emails.size()) {
                        Email number = emails.get(emailIndex);
                        contact.removeEmail(number);
                    } else {
                        System.out.println("Incorrect element index");
                    }
                }
            }
            case "address" -> contact.setAddress(null);
            case "organisation" -> contact.setOrganisation("");
            case "birth-date" -> contact.setBirthDate(null);
            case "note" -> contact.setNote("");
            default -> System.out.println("Incorrect instructions");
        }
    }

    private void searchByPattern(String input) throws DatabaseOperationException {
        String pattern = input.substring(6).trim();
        List<Contact> contacts = DATABASE.searchByPattern(pattern);
        if (contacts.isEmpty()) {
            System.out.println("No matches found");
        } else {
            printContactList(contacts);
        }
    }

    private void importFromFile(String inputLine) throws FileOperationException {
        String fileName = inputLine.substring(6).trim();
        List<Contact> importedContacts = FileUtils.importContactsFromVcard(fileName);
        for (Contact contact : importedContacts) {
            try {
                DATABASE.saveContact(contact);
                System.out.println("Successfully saved " + contact.getName());
            } catch (DatabaseOperationException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void printContactList(List<Contact> contactList) {
        for (int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            System.out.printf("%n%d. %s (%s)",
                    i + 1,
                    contact.getName(),
                    contact.getNumbers().size() > 0 ? contact.getNumbers().get(0) : "no number");
        }
        System.out.println("\n");
    }

    private void printHelpMessage() {
        System.out.println("""
                Adding contact
                To add contact type: add
                Then you will be asked for contact's field values. Setting name is mandatory.
                To skip entering other information press Enter.

                Getting all contacts list
                To get all contacts list(name and number) type: get all
                                
                Searching for contacts
                To search for contact type: search PATTERN
                As PATTERN you can use either name, number or email's fragment.
                                
                Accessing contact
                To access specific contact type: access CONTACT_NAME
                After that you will be able to edit contact with: edit command, or delete it with: delete
                To return from contact's view type: return
                                
                Importing contacts
                To import contacts from vCard (.vcf file) type: import FILE_NAME

                Other available commands:
                help   - shows this message
                exit   - closes program
                """);
    }
}
