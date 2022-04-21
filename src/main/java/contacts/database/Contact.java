package contacts.database;

import contacts.utils.EmailValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@NoArgsConstructor
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String name;
    private String number;
    @Pattern(regexp = EmailValidator.RFC_5322_REGEX)
    private String email = "";
    private String organisation = "";
    private String address = "";
    private String birthDate = "";
    private String note = "";

    @Override
    public String toString() {
        return "{\n" +
                "        name: " + name + "\n" +
                "      number: " + number + "\n" +
                "       email: " + email + "\n" +
                "     address: " + address + "\n" +
                "  birth date: " + birthDate + "\n" +
                "        note: " + note + "\n" +
                "}";
    }
}
