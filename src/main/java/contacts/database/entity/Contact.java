package contacts.database.entity;

import contacts.database.exception.ContactParseException;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "contact",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<PhoneNumber> numbers = new ArrayList<>();

    @OneToMany(mappedBy = "contact",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Email> emails = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    private String organisation = "";

    private LocalDate birthDate;

    private String note = "";

    public void addNumber(PhoneNumber phoneNumber) {
        if (phoneNumber != null) {
            if (!phoneNumber.isEmpty()) {
                numbers.add(phoneNumber);
                phoneNumber.setContact(this);
            }
        }
    }

    public void removeNumber(PhoneNumber phoneNumber) {
        numbers.remove(phoneNumber);
        phoneNumber.setContact(null);
    }

    public void addEmail(Email email) {
        if (email != null) {
            if (!email.isEmpty()) {
                emails.add(email);
                email.setContact(this);
            }
        }
    }

    public void removeEmail(Email email) {
        emails.add(email);
        email.setContact(null);
    }

    public void setAddress(Address address) {
        if (address == null) {
            Address removedAddress = this.address;
            this.address = null;
            removedAddress.setContact(null);
        } else if (!address.isEmpty()) {
            this.address = address;
            address.setContact(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n name: ").append(name).append("\n");

        if (numbers.size() == 0) {
            stringBuilder.append(" number: \n");
        } else if (numbers.size() == 1) {
            stringBuilder.append(" number: ").append(numbers.get(0)).append("\n");
        } else {
            stringBuilder.append(" numbers:\n");
            int index = 1;
            for (PhoneNumber number : numbers) {
                stringBuilder.append("  ").append(index).append(". ").append(number).append("\n");
                index++;
            }
        }

        if (emails.size() == 0) {
            stringBuilder.append(" email: \n");
        } else if (emails.size() == 1) {
            stringBuilder.append(" email: ").append(emails.get(0)).append("\n");
        } else {
            stringBuilder.append(" emails:\n");
            int index = 1;
            for (Email email : emails) {
                stringBuilder.append("  ").append(index).append(". ").append(email).append("\n");
                index++;
            }
        }

        stringBuilder.append(" address: ").append(Objects.requireNonNullElse(address, "")).append("\n")
                .append(" organisation: ").append(organisation).append("\n")
                .append(" birth date: ").append(Objects.requireNonNullElse(birthDate, "")).append("\n")
                .append(" note: ").append(note).append("\n}\n");

        return stringBuilder.toString();
    }

    static public Contact parseFromVcard(VCard vCard) throws ContactParseException {
        Contact contact = new Contact();

        FormattedName cardName = vCard.getFormattedName();
        if (cardName == null) {
            throw new ContactParseException("Contact name not specified");
        }
        contact.setName(cardName.getValue());

        for (Telephone number : vCard.getTelephoneNumbers()) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setNumber(number.getText());
            List<TelephoneType> types = number.getTypes();
            if (!types.isEmpty()) {
                phoneNumber.setType(types.get(0).getValue());
            }
            contact.addNumber(phoneNumber);
        }

        for (ezvcard.property.Email cardEmail : vCard.getEmails()) {
            contacts.database.entity.Email email = new contacts.database.entity.Email();
            email.setEmail(cardEmail.getValue());
            contact.addEmail(email);
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

        List<ezvcard.property.Address> cardAddresses = vCard.getAddresses();
        for (ezvcard.property.Address cardAddress : cardAddresses) {
            contacts.database.entity.Address address = new contacts.database.entity.Address();
            address.setStreet(cardAddress.getStreetAddress());
            address.setCity(cardAddress.getLocality());
            address.setCountry(cardAddress.getCountry());
            address.setPostalCode(cardAddress.getPostalCode());
            contact.setAddress(address);
        }

        Birthday cardBirthday = vCard.getBirthday();
        if (cardBirthday != null) {
            contact.setBirthDate(cardBirthday.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        List<Note> cardNotes = vCard.getNotes();
        StringBuilder stringBuilder = new StringBuilder();
        for (Note note : cardNotes) {
            stringBuilder.append(note.getValue()).append(",");
        }
        contact.setNote(stringBuilder.toString());

        return contact;
    }
}
