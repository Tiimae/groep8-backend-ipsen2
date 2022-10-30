package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {
    private final UserDAO userDAO;
    private final MeetingRoomDAO meetingRoomDAO;
    private final WingDAO wingDAO;

    public ReservationMapper(UserDAO userDAO, MeetingRoomDAO meetingRoomDAO, WingDAO wingDAO) {
        this.userDAO = userDAO;
        this.meetingRoomDAO = meetingRoomDAO;
        this.wingDAO = wingDAO;
    }

    public Reservation toReservation(ReservationDTO reservationDTO) throws EntryNotFoundException {
        //required parameters
        LocalDateTime starttime = LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDTO.getStarttime()), TimeZone.getDefault().toZoneId());
        LocalDateTime endtime = LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDTO.getEndtime()), TimeZone.getDefault().toZoneId());

        Optional<User> userEntry = userDAO.getUserFromDatabase(reservationDTO.getUserId());
        if (userEntry.isEmpty()) throw new EntryNotFoundException("User not found.");
        User user = userEntry.get();

        //optional parameters
        Set<MeetingRoom> meetingRooms = new HashSet<>();
        if (reservationDTO.getMeetingRoomIds() != null) {
            meetingRooms = Arrays.stream(reservationDTO.getMeetingRoomIds())
                    .map(id -> meetingRoomDAO.getMeetingRoomFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        Wing wing = null;
        if (reservationDTO.getWingId() != null) {
            Optional<Wing> wingEntry = wingDAO.getWingFromDatabase(reservationDTO.getWingId());
            if (wingEntry.isEmpty()) throw new EntryNotFoundException("Wing not found.");
            wing = wingEntry.get();
        }

        boolean status = false;
        if (reservationDTO.getStatus() != null) status = reservationDTO.getStatus();

        int amount = 0;
        if (reservationDTO.getAmount() != null) amount =  reservationDTO.getAmount();

        String note = reservationDTO.getNote();

        return new Reservation(starttime, endtime, status, amount, note, user, meetingRooms, wing);
    }

    public Reservation mergeReservations(Reservation base, Reservation update) {
        base.setStartDate(update.getStartDate());
        base.setEndDate(update.getEndDate());
        base.setAmount(update.getAmount());
        base.setStatus(update.isStatus());
        base.setNote(update.getNote());
        base.setUser(update.getUser());
        base.setMeetingRooms(update.getMeetingRooms());
        base.setWing(update.getWing());

        return base;
    }
}
