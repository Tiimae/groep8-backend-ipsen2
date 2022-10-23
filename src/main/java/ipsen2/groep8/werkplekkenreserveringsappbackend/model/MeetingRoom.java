package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meetingRoom")
@Getter
@Setter
public class MeetingRoom {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private Long amountPeople;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Wing wing;

    @ManyToMany
    @JoinTable(
            name = "meetingRoomReservations",
            joinColumns = @JoinColumn(name = "meetingroom_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id")
    )
    private Set<Reservation> reservations = new HashSet<>();

    public MeetingRoom() { }

    public MeetingRoom(Long amountPeople, Wing wing, Set<Reservation> reservations) {
        this.amountPeople = amountPeople;
        this.wing = wing;

        for (Reservation reservation : reservations) {
            this.addReservation(reservation);
        }
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            this.getReservations().add(reservation);
            reservation.getMeetingRooms().add(this);
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null) {
            this.getReservations().remove(reservation);
            reservation.getMeetingRooms().remove(this);
        }
    }
}
