package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"))
    private Set<User> users;

    @ManyToMany
    @JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "permissionid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"))
    private Set<Permission> permissions;

    public Role() { }

    public Role(String name, Set<User> users, Set<Permission> permissions) {
        this.name = name;
        this.users = users;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
