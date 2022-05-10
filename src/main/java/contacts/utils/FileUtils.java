package contacts.utils;

import contacts.database.entity.Contact;
import ezvcard.VCard;
import ezvcard.io.text.VCardReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    static public List<Contact> importContactsFromVcard(String fileName) throws FileOperationException {
        List<Contact> contactList = new ArrayList<>();
        File file = new File(fileName);
        try (VCardReader reader = new VCardReader(file)) {
            VCard vCard;
            while ((vCard = reader.readNext()) != null) {
                Contact contact = Contact.parseFromVcard(vCard);
                contactList.add(contact);
            }
            return contactList;
        } catch (Exception exception) {
            throw new FileOperationException("Could not load contacts from file: " + fileName);
        }
    }
}
