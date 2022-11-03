package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permission")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    public Permission() { }

    public Permission(String name, Set<Role> roles) {
        this.name = name;

        for (Role role : roles) {
            this.addRole(role);
        }
    }

    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
            role.getPermissions().add(this);
        }
    }

    public void removeRole(Role role) {
        if (role != null) {
            this.roles.remove(role);
            role.getPermissions().remove(this);
        }
    }
}
