package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnoreProperties
    private String password;

    @NotNull
    private Boolean verified;

    @NotNull
    private Boolean reset_required;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("users")
    private Department department;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonBackReference
    private Set<Favorite> favorite = new HashSet<>();

    @OneToMany(mappedBy = "favorite", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonBackReference
    private Set<Favorite> favoriteOf = new HashSet<>();

    public User() {
    }

    public User(String name, String email, String password, Boolean verified, Boolean resetRequired, Set<Role> roles, Department department, Set<Reservation> reservations) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.reset_required = resetRequired;
        this.department = department;

        for (Role role : roles) {
            this.addRoles(role);
        }

        for (Reservation reservation : reservations) {
            this.addReservation(reservation);
        }
    }

    public void addRoles(Role role) {
        if (role != null) {
            this.roles.add(role);
            role.getUsers().add(this);
        }
    }


    public void removeRoles(Role role) {
        if (role != null) {
            this.roles.remove(role);
            role.getUsers().remove(this);
        }
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            this.reservations.add(reservation);
            reservation.setUser(this);
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null) {
            this.reservations.remove(reservation);
            reservation.setUser(this);
        }
    }
}
