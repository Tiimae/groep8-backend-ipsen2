package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.ReservationRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReservationDAO {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper mapper;

    public ReservationDAO(ReservationRepository reservationRepository, @Lazy ReservationMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.mapper = mapper;
    }

    public Optional<Reservation> getReservationFromDatabase(String id) {
        return this.reservationRepository.findById(id);
    }

    public List<Reservation> getAllReservations() {
        return this.reservationRepository.findAll();
    }

    public void saveReservationToDatabase(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    public Reservation updateReservationInDatabase(String id, Reservation updateReservation) {
        Reservation reservation = this.reservationRepository.getById(id);
        mapper.mergeReservations(reservation, updateReservation);
        reservationRepository.saveAndFlush(reservation);

        return reservation;
    }

    public void deleteReservationFromDatabase(String id) {
        final Reservation reservation = this.reservationRepository.getById(id);

        reservation.getUser().getReservations().remove(reservation);
        reservation.setUser(null);

        reservation.getWing().getReservations().remove(reservation);
        reservation.setWing(null);

        for (MeetingRoom meetingRoom : reservation.getMeetingRooms()) {
            reservation.getMeetingRooms().remove(meetingRoom);
            meetingRoom.getReservations().remove(reservation);
        }

        this.reservationRepository.deleteById(id);
    }
}
