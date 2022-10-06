package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "wing")
public class Wing {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private String name;
    private Long workplaces;
    private Long floor;


    @ManyToMany
    @JoinTable(name = "department_wings", joinColumns = @JoinColumn(name = "departmentid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "wingid", referencedColumnName = "id"))
    @JsonBackReference
    private Set<Department> departments;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Building building;


    @OneToMany(mappedBy = "wing", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<MeetingRoom> meetingRooms;

    @OneToMany(mappedBy = "wing", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Reservation> reservations;

    public Wing() { }

    public Wing(String name, Long workplaces, Long floor, Set<Department> departments, Building building, Set<MeetingRoom> meetingRooms, Set<Reservation> reservations) {
        this.name = name;
        this.workplaces = workplaces;
        this.floor = floor;
        this.departments = departments;
        this.building = building;
        this.meetingRooms = meetingRooms;
        this.reservations = reservations;
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

    public Long getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(Long workplaces) {
        this.workplaces = workplaces;
    }

    public Long getFloor() {
        return floor;
    }

    public void setFloor(Long floor) {
        this.floor = floor;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    public void setMeetingRooms(Set<MeetingRoom> meetingRooms) {
        this.meetingRooms = meetingRooms;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
