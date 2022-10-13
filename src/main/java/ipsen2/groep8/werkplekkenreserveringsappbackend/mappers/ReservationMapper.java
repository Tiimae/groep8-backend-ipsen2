package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.MeetingRoomRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ReservationMapper {
    private final UserRepository userRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final WingRepository wingRepository;

    public ReservationMapper(UserRepository userRepository, MeetingRoomRepository meetingRoomRepository, WingRepository wingRepository) {
        this.userRepository = userRepository;
        this.meetingRoomRepository = meetingRoomRepository;
        this.wingRepository = wingRepository;
    }

    public Reservation toReservation (ReservationDTO reservationDTO) {
        var starttime = LocalDateTime.ofEpochSecond(reservationDTO.getStarttime(), 0, ZoneOffset.UTC);
        var endtime = LocalDateTime.ofEpochSecond(reservationDTO.getStarttime(), 0, ZoneOffset.UTC);

        User user = userRepository.getById(reservationDTO.getUserId());
        //TODO: meetingroom should have string id but is long
//        Set<MeetingRoom> meetingRooms = Arrays.stream(reservationDTO.getMeetingRoomIds())
//                        .map(id -> meetingRoomRepository.getById(id))
//                        .collect(Collectors.toSet());
        Wing wing = wingRepository.getById(reservationDTO.getWingId());

        //TODO: replace null with valid meetingrooms
        return new Reservation(starttime, endtime, reservationDTO.isStatus(), reservationDTO.getAmount(), reservationDTO.getNote(), user, null, wing);
    }
}
