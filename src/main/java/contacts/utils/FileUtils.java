package contacts.utils;

import contacts.database.Contact;
import ezvcard.VCard;
import ezvcard.io.text.VCardReader;
import ezvcard.property.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    static public List<Contact> loadContactsFromVcard(String fileName) throws FileOperationException {
        List<Contact> contactList = new ArrayList<>();
        File file = new File(fileName);

        try (VCardReader reader = new VCardReader(file)) {
            VCard vCard;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            while ((vCard = reader.readNext()) != null) {

                Contact contact = new Contact();

                FormattedName cardName = vCard.getFormattedName();
                if (cardName == null) {
                    continue;
                }
                contact.setName(cardName.getValue());

                List<Telephone> cardNumbers = vCard.getTelephoneNumbers();
                if (cardNumbers.isEmpty()) {
                    continue;
                }
                contact.setNumber(cardNumbers.get(0).getText());

                List<Email> cardEmail = vCard.getEmails();
                if (!cardEmail.isEmpty()) {
                    contact.setEmail(cardEmail.get(0).getValue());
                }

                Organization cardOrganization = vCard.getOrganization();
                if (cardOrganization != null) {
                    List<String> organizationParams = cardOrganization.getValues();
                    StringBuilder builder = new StringBuilder();
                    for (String param : organizationParams) {
                        builder.append(param).append(",");
                    }
                    contact.setOrganisation(builder.toString());
                }

                List<Address> cardAddresses = vCard.getAddresses();
                if (!cardAddresses.isEmpty()) {
                    contact.setAddress(cardAddresses.get(0).getExtendedAddressFull());
                }

                Birthday cardBirthday = vCard.getBirthday();
                if (cardBirthday != null) {
                    contact.setBirthDate(dateFormat.format(cardBirthday.getDate()));
                }

                List<Note> cardNotes = vCard.getNotes();
                StringBuilder builder = new StringBuilder();
                for (Note note : cardNotes) {
                    builder.append(note.getValue()).append(",");
                }
                contact.setNote(builder.toString());
                contactList.add(contact);
            }
            return contactList;
        } catch (Exception exception) {
            throw new FileOperationException("Could not load contacts from file: " + fileName);
        }
    }
}
