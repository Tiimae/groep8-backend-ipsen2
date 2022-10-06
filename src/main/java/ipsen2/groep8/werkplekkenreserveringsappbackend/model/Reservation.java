package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean status;

    @Column(length = 2)
    @Value("1")
    private int amount;

    @Column(columnDefinition = "TEXT")
    private String note;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Wing wing;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(name = "reservaton_meetingrooms", joinColumns = @JoinColumn(name = "meetingroomid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "reservationid", referencedColumnName = "id"))
    @JsonBackReference
    private Set<MeetingRoom> meetingRooms;

    public Reservation() { }

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, boolean status, int amount, String note, Wing wing, User user, Set<MeetingRoom> meetingRooms) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.wing = wing;
        this.user = user;
        this.meetingRooms = meetingRooms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Wing getWing() {
        return wing;
    }

    public void setWing(Wing wing) {
        this.wing = wing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    public void setMeetingRooms(Set<MeetingRoom> meetingRooms) {
        this.meetingRooms = meetingRooms;
    }
}
