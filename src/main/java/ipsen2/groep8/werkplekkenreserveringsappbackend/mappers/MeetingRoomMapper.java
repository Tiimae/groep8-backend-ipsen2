package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.MeetingRoomDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeetingRoomMapper {

    /**
     * This is the variable for the WingDAO in the class
     */
    private WingDAO wingDAO;

    /**
     * This is the variable for the ReservationDAO in the class
     */
    private ReservationDAO reservationDAO;

    /**
     * This is the constructor of the MeetingRoomMapper. It set the WingDAO and ReservationDAO
     *
     * @param wingDAO The DAO for wing
     * @param reservationDAO The DAO for reservation
     * @author Tim de Kok
     */
    public MeetingRoomMapper(WingDAO wingDAO, ReservationDAO reservationDAO) {
        this.wingDAO = wingDAO;
        this.reservationDAO = reservationDAO;
    }

    /**
     * This function will be used to create a new meeting room in the database
     *
     * @param meetingRoomDTO The meeting room data to create a new meeting room
     * @return a new meeting room
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    public MeetingRoom toMeetingRoom(MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {
        String name = meetingRoomDTO.getName();
        Long amountPeople = meetingRoomDTO.getAmountPeople();
        Wing wing = this.getWing(meetingRoomDTO.getWingId());
        Set<Reservation> reservations = this.getAllReservations(meetingRoomDTO.getReservationIds());

        return new MeetingRoom(name, amountPeople, wing, reservations);

    }

    /**
     * This function returns an update meeting room, so we can save it in the database
     *
     * @param base The meeting room data that already exist in the database
     * @param update The meeting room data to create a new meeting room
     * @return an updated meeting room
     * @author Tim de Kok
     */
    public MeetingRoom mergeMeetingRoom(MeetingRoom base, MeetingRoomDTO update) throws EntryNotFoundException {
        base.setName(update.getName());
        base.setAmountPeople(update.getAmountPeople());

        if (this.getWing(update.getWingId()) != null) {
            base.setWing(this.getWing(update.getWingId()));
        }

        return base;
    }

    public Set<Reservation> getAllReservations(String[] reservationIds) {
        Set<Reservation> reservations = new HashSet<>();
        if (reservationIds != null) {
            reservations = Arrays.stream(reservationIds)
                    .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return reservations;
    }

    public Wing getWing(String wingId) throws EntryNotFoundException {
        Wing wing = null;

        if (wingId != null) {
            final Optional<Wing> wingOptional = this.wingDAO.getWingFromDatabase(wingId);
            if (wingOptional.isEmpty()) {
                throw new EntryNotFoundException("Wing not found.");
            }

            wing = wingOptional.get();
        }

        return wing;
    }
}
