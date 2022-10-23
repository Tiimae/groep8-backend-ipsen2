package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.MeetingRoomDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
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

    private WingDAO wingDAO;
    private ReservationDAO reservationDAO;

    public MeetingRoomMapper(WingDAO wingDAO, ReservationDAO reservationDAO) {
        this.wingDAO = wingDAO;
        this.reservationDAO = reservationDAO;
    }

    public MeetingRoom toMeetingRoom(MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {
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

        return new MeetingRoom(amountPeople, wing, reservations);

    }

    public MeetingRoom mergeMeetingRoom(MeetingRoom base, MeetingRoom update) {
        base.setAmountPeople(update.getAmountPeople());
        base.setWing(update.getWing());
        base.setReservations(update.getReservations());

        return base;
    }

}
