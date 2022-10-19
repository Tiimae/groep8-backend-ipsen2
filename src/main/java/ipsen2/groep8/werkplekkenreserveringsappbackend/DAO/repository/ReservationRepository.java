package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
