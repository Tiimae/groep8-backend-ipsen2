package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Entity
@Table(name = "department")
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "departments")
    @JsonIgnoreProperties("departments")
    private Set<Wing> wings = new HashSet<>();

    public Department() { }

    public Department(String name, Set<User> users, Set<Wing> wings) {
        this.name = name;

        for (User user : users) {
            this.addUser(user);
        }

        for (Wing wing : wings) {
            this.addWing(wing);
        }
    }

    private void addUser(User user) {
        if (user != null) {
            this.getUsers().add(user);
            user.setDepartment(this);
        }
    }

    private void removeUser(User user) {
        if (user != null) {
            this.getUsers().remove(user);
            user.setDepartment(null);
        }
    }

    private void addWing(Wing wing) {
        if (wing != null) {
            this.getWings().add(wing);
            wing.getDepartments().add(this);
        }
    }

    private void removeWing(Wing wing) {
        if (wing != null) {
            this.getWings().remove(wing);
            wing.getDepartments().remove(this);
        }
    }


}
