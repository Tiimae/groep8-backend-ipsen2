package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "building")
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

    @OneToOne(mappedBy = "building", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Variable variable;

    public Building() { }

    public Building(String name, String address, String zipcode, String city, Set<Wing> wings, Variable variable) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.wings = wings;
        this.variable = variable;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Wing> getWings() {
        return wings;
    }

    public void setWings(Set<Wing> wings) {
        this.wings = wings;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
