package contacts.database.entity;

import contacts.utils.EmailValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Pattern(regexp = EmailValidator.RFC_5322_REGEX, message = "Email does not match RFC 5322 email pattern")
    private String email = "";

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Override
    public String toString() {
        return email;
    }

    public boolean isEmpty() {
        return email.isEmpty();
    }

}
