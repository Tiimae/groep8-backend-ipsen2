package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.ReservationRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReservationDAO {
    private final ReservationRepository reservationRepository;

    public ReservationDAO(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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

    public void updateReservationInDatabase(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    public void deleteReservationFromDatabase(String id) {
        this.reservationRepository.deleteById(id);
    }
}
