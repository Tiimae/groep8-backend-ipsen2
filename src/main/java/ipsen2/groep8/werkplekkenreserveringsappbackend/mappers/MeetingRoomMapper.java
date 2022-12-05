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

        Wing wing = null;
        if (meetingRoomDTO.getWingId() != null) {
            final Optional<Wing> wingOptional = this.wingDAO.getWingFromDatabase(meetingRoomDTO.getWingId());
            if (wingOptional.isEmpty()) {
                throw new EntryNotFoundException("Wing not found.");
            }

            wing = wingOptional.get();
        }

        Set<Reservation> reservations = new HashSet<>();
        reservations = Arrays.stream(meetingRoomDTO.getReservationIds())
                .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

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
    public MeetingRoom mergeMeetingRoom(MeetingRoom base, MeetingRoom update) {
        base.setAmountPeople(update.getAmountPeople());
        base.setWing(update.getWing());
        base.setReservations(update.getReservations());

        return base;
    }

}
