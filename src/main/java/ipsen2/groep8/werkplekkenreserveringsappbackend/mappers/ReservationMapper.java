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
        LocalDateTime starttime = LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDTO.getStarttime()), TimeZone.getDefault().toZoneId());
        LocalDateTime endtime = LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDTO.getEndtime()), TimeZone.getDefault().toZoneId());
        String type = reservationDTO.getType();
        User user = this.getUser(reservationDTO.getUserId());
        Set<MeetingRoom> meetingRooms = this.getAllMeetingRooms(reservationDTO.getMeetingRoomIds());
        Wing wing = this.getWing(reservationDTO.getWingId());

        boolean status = false;
        if (reservationDTO.getStatus() != null) status = reservationDTO.getStatus();

        int amount = 0;
        if (reservationDTO.getAmount() != null) amount = reservationDTO.getAmount();

        String note = reservationDTO.getNote();

        return new Reservation(starttime, endtime, status, amount, note, user, meetingRooms, wing, type);
    }

    public Reservation mergeReservations(Reservation base, ReservationDTO update) throws EntryNotFoundException {
        base.setStartDate( LocalDateTime.ofInstant(Instant.ofEpochMilli(update.getStarttime()), TimeZone.getDefault().toZoneId()));
        base.setEndDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(update.getEndtime()), TimeZone.getDefault().toZoneId()));
        base.setAmount(update.getAmount());
        base.setStatus(update.getStatus());
        base.setType(update.getType());
        base.setNote(update.getNote());

        base.getMeetingRooms().clear();

        base.setUser(this.getUser(update.getUserId()));
        base.setMeetingRooms(this.getAllMeetingRooms(update.getMeetingRoomIds()));
        base.setWing(this.getWing(update.getWingId()));

        return base;
    }

    public User getUser(String userId) throws EntryNotFoundException {
        User user = null;

        Optional<User> userEntry = userDAO.getUserFromDatabase(userId);
        if (userEntry.isEmpty()) throw new EntryNotFoundException("User not found.");
        user = userEntry.get();

        return user;
    }

    public Wing getWing(String wingId) throws EntryNotFoundException {
        Wing wing = null;
        if (wingId != null) {
            Optional<Wing> wingEntry = wingDAO.getWingFromDatabase(wingId);
            if (wingEntry.isEmpty()) throw new EntryNotFoundException("Wing not found.");
            wing = wingEntry.get();
        }

        return wing;
    }

    public Set<MeetingRoom> getAllMeetingRooms(String[] meetingRoomIds) {
        Set<MeetingRoom> meetingRooms = new HashSet<>();
        if (meetingRoomIds != null) {
            meetingRooms = Arrays.stream(meetingRoomIds)
                    .map(id -> this.meetingRoomDAO.getMeetingRoomFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return meetingRooms;
    }
}
