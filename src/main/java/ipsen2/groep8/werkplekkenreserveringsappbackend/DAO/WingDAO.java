package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.DepartmentRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.MeetingRoomRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.WingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class WingDAO {
    private WingRepository wingRepository;
    private WingMapper wingMapper;
    private ReservationDAO reservationDAO;

    public WingDAO(WingRepository wingRepository, @Lazy WingMapper wingMapper, ReservationDAO reservationDAO) {
        this.wingRepository = wingRepository;
        this.wingMapper = wingMapper;
        this.reservationDAO = reservationDAO;
    }

    public Optional<Wing> getWingFromDatabase(String wingId) {
        return this.wingRepository.findById(wingId);
    }

    public List<Wing> getAllWingsFromDatabase() {
        return this.wingRepository.findAll();
    }

    public void saveWingToDatabase(Wing wing) {
        this.wingRepository.save(wing);
    }

    public void updateWingInDatabase(String id, Wing wingUpdate) {
        final Wing wing = this.wingMapper.mergeWing(this.wingRepository.getById(id), wingUpdate);
        this.wingRepository.saveAndFlush(wing);
    }

    public void deleteWingFromDatabase(String wingId) {
        final Wing wing = this.wingRepository.getById(wingId);

        wing.getBuilding().getWings().remove(wing);
        wing.setBuilding(null);

        for (Reservation reservation : wing.getReservations()) {
            this.reservationDAO.deleteReservationFromDatabase(reservation.getId());
        }

        wing.getReservations().clear();

        this.wingRepository.deleteById(wingId);
    }
}
