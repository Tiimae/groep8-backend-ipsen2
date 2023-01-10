package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim de Kok, Wouter de Bruijn
 * @version 1.0
 */
@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    private boolean status;

    @Column(length = 2)
    @Value("1")
    @NotNull
    private int amount;

    @Column(columnDefinition = "TEXT")
    private String note;

    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    TODO: write propper mapper and DTO
    @JsonIgnoreProperties("reservations")
//    @JsonManagedReference
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservations")
//    TODO: write propper mapper and DTO
    @JsonIgnoreProperties("reservations")
//    @JsonManagedReference
    private Set<MeetingRoom> meetingRooms = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    TODO: write propper mapper and DTO
    @JsonIgnoreProperties("reservations")
    @JsonManagedReference
    private Wing wing;

    public Reservation() { }

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, boolean status, int amount, String note, User user, Set<MeetingRoom> meetingRooms, Wing wing, String type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.user = user;
        this.wing = wing;
        this.type = type;

        for (MeetingRoom meetingRoom : meetingRooms) {
            this.addMeetingRoom(meetingRoom);
        }
    }

    public void addMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRooms.add(meetingRoom);
        meetingRoom.getReservations().add(this);
    }

    public void removeMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRooms.remove(meetingRoom);
        meetingRoom.getReservations().remove(this);
    }

//    @JsonBackReference(value = "wing-reservation")
    public Wing getWing() {
        return wing;
    }

//    @JsonBackReference(value = "wing-reservation")
    public void setWing(Wing wing) {
        this.wing = wing;
    }
}
