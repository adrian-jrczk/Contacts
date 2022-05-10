package contacts.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String street = "";
    String city = "";
    String country = "";
    String postalCode = "";

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Override
    public String toString() {
        return "street='" + Objects.requireNonNullElse(street, "") + "', " +
                "city='" + Objects.requireNonNullElse(city, "") + "', " +
                "state='" + Objects.requireNonNullElse(country, "") + "', " +
                "postal code='" + Objects.requireNonNullElse(postalCode, "") + "'";
    }

    public boolean isEmpty() {
        return street.isEmpty() && city.isEmpty() && country.isEmpty() && postalCode.isEmpty();
    }
}
