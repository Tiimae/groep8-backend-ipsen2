package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "wing")
@Getter
@Setter
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
    @JoinTable(
            name = "departments_wings",
            joinColumns = @JoinColumn(name = "wing_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Building building;

    @OneToMany(mappedBy = "wing", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "wing", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<MeetingRoom> meetingRooms;

    public Wing() {
    }

    public Wing(String name, Long workplaces, Long floor, Set<Department> departments, Building building, Set<MeetingRoom> meetingRooms, Set<Reservation> reservations) {
        this.name = name;
        this.workplaces = workplaces;
        this.floor = floor;
        this.building = building;

        for (Department department : departments ) {
            this.addDepartment(department);
        }

        for (MeetingRoom meetingRoom : meetingRooms) {
            this.addMeetingRoom(meetingRoom);
        }

        for (Reservation reservation : reservations) {
            this.addReservation(reservation);
        }
    }

    public void addDepartment(Department department) {
        if (department != null) {
            this.departments.add(department);
            department.getWings().add(this);
        }
    }

    public void removeDepartment(Department department) {
        if (department != null) {
            this.departments.remove(department);
            department.getWings().remove(this);
        }
    }


    public void addMeetingRoom(MeetingRoom meetingRoom) {
        if (meetingRoom != null) {
            this.meetingRooms.add(meetingRoom);
            meetingRoom.setWing(this);
        }
    }

    public void removeMeetingRoom(MeetingRoom meetingRoom) {
        if (meetingRoom != null) {
            this.meetingRooms.remove(meetingRoom);
            meetingRoom.setWing(null);
        }
    }


    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            this.reservations.add(reservation);
            reservation.setWing(this);
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null) {
            this.reservations.remove(reservation);
            reservation.setWing(null);
        }
    }

    @JsonManagedReference(value = "wing-reservation")
    public Set<Reservation> getReservations() {
        return reservations;
    }

    @JsonManagedReference(value = "wing-reservation")
    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
