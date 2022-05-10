package contacts.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "number")
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String number;
    String type;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Override
    public String toString() {
        if (type.isEmpty()) {
            return number;
        } else {
            return number + " [type=" + type + "]";
        }
    }

    public boolean isEmpty() {
        return number.isEmpty();
    }
}
