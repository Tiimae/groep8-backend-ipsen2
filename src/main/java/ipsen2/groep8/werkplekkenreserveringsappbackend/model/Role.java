package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(unique = true)
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("roles")
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "permissionid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id")
    )
    private Set<Permission> permissions = new HashSet<>();

    public Role() { }

    public Role(String name, Set<User> users, Set<Permission> permissions) {
        this.name = name;

        for (User user : users) {
            this.addUser(user);
        }

        for (Permission permission : permissions) {
            this.addPermission(permission);
        }
    }

    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            this.users.remove(user);
            user.getRoles().remove(this);
        }
    }

    public void addPermission(Permission permission) {
        if (permission != null) {
            this.permissions.add(permission);
            permission.getRoles().add(this);
        }
    }

    public void removePermission(Permission permission) {
        if (permission != null) {
            this.permissions.remove(permission);
            permission.getRoles().remove(this);
        }
    }
}
