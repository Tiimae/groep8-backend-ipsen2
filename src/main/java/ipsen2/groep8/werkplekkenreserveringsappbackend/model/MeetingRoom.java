package ipsen2.groep8.werkplekkenreserveringsappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "meetingRoom")
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
    @JoinTable(name = "reservaton_meetingrooms", joinColumns = @JoinColumn(name = "meetingroomid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "reservationid", referencedColumnName = "id"))
    @JsonBackReference
    private Set<Reservation> reservations;

    public MeetingRoom() { }

    public MeetingRoom(Long amountPeople, Wing wing, Set<Reservation> reservations) {
        this.amountPeople = amountPeople;
        this.wing = wing;
        this.reservations = reservations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(Long amountPeople) {
        this.amountPeople = amountPeople;
    }

    public Wing getWing() {
        return wing;
    }

    public void setWing(Wing wing) {
        this.wing = wing;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
