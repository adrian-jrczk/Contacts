package contacts.console;

import contacts.database.Contact;
import contacts.database.Database;
import contacts.database.DatabaseOperationException;
import contacts.utils.FileOperationException;
import contacts.utils.FileUtils;
import java.util.List;

public class ConsoleInterface {

    private final Database DATABASE = new Database();

    public void open() {
        while (true) {
            try {
                String inputLine = ConsoleInput.getInstructions();
                switch (InputRecognizer.recognize(inputLine)) {
                    case ADD -> addContact();
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
        newContact.setNumber(ConsoleInput.getNumber());
        newContact.setEmail(ConsoleInput.getEmail());
        newContact.setAddress(ConsoleInput.getAddress());
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
            String inputLine = ConsoleInput.getInstructions();
            switch (InputRecognizer.recognize(inputLine)) {
                case EDIT_FIELD :
                    String fieldToChange = inputLine.substring(4).toLowerCase().trim();
                    switch (fieldToChange) {
                        case "name" -> contact.setName(ConsoleInput.getName());
                        case "number" -> contact.setNumber(ConsoleInput.getNumber());
                        case "email" -> contact.setEmail(ConsoleInput.getEmail());
                        case "address" -> contact.setAddress(ConsoleInput.getAddress());
                        case "birth date" -> contact.setBirthDate(ConsoleInput.getBirthDate());
                        case "note" -> contact.setNote(ConsoleInput.getNote());
                    }
                    DATABASE.updateContact(contact);
                    System.out.println(contact);
                    break;
                case DELETE:
                    DATABASE.deleteContact(contact);
                case RETURN:
                    return;
                default:
                    System.out.println("""
                            Incorrect input.
                            You can use following commands:
                            edit FIELD_NAME
                            delete
                            return
                            
                            """);
            }
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
        List<Contact> importedContacts = FileUtils.loadContactsFromVcard(fileName);
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
            System.out.printf("%n%d. %s [%s]", i + 1, contact.getName(), contact.getNumber());

        }
        System.out.println("\n");
    }

    private void printHelpMessage() {
        System.out.println("""
                Adding contact
                To add contact type: add
                Then you will be asked for contact's field values. Setting name and number field is mandatory.
                To skip entering other information press Enter.

                Getting all contacts list
                To get all contacts list(name and number) type: get all
                
                Searching for contacts
                To search for contact type: search PATTERN
                As PATTERN you can use either name, number or email's fragment.
                
                Accessing contact
                To access specific contact type: access CONTACT_NAME
                After that you will be able to edit contact trough: edit FIELD_NAME, or delete it with: delete
                To return from contact's view type: return
                
                Importing contacts
                To import contacts from vCard (.vcf file) type: import FILE_NAME

                Other available commands:
                help   - shows this message
                exit   - closes program
                """);
    }
}
