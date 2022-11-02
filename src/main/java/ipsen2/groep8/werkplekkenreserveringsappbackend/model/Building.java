package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "building")
@Getter
@Setter
public class Building {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private String name;
    private String address;

    @Column(columnDefinition = "VARCHAR(6)")
    private String zipcode;
    private String city;

    @OneToMany(mappedBy = "building", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Wing> wings;


    public Building() { }

    public Building(String name, String address, String zipcode, String city, Set<Wing> wings) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;

        for (Wing wing : wings) {
            this.addWing(wing);
        }
    }

    private void addWing(Wing wing) {
        if (wing != null) {
            this.getWings().add(wing);
            wing.setBuilding(this);
        }
    }

    private void removeWing(Wing wing) {
        if (wing != null) {
            this.getWings().remove(wing);
            wing.setBuilding(null);
        }
    }
}
