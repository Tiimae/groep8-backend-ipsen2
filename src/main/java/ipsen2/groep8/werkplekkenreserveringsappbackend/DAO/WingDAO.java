package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.WingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WingDAO {

    /**
     * This is the variable for the WingRepository in the class
     */
    private WingRepository wingRepository;

    /**
     * This is the variable for the WingMapper in the class
     */
    private WingMapper wingMapper;

    /**
     * This is the variable for the ReservationDAO in the class
     */
    private ReservationDAO reservationDAO;

    /**
     * This is the constructor of the wingDAO. It set the wingMapper, wingRepository and reservationDAO
     *
     * @param wingMapper The Mapper for wing
     * @param wingRepository The repository for wing
     * @param reservationDAO The DAO for wing
     * @author Tim de Kok
     */
    public WingDAO(WingRepository wingRepository, @Lazy WingMapper wingMapper, ReservationDAO reservationDAO) {
        this.wingRepository = wingRepository;
        this.wingMapper = wingMapper;
        this.reservationDAO = reservationDAO;
    }

    /**
     * Get a specific wing out of the database by id and returns that wing
     *
     * @param wingId The id of the wing you want to have
     * @return A wing out of the database
     * @author Tim de Kok
     */
    public Optional<Wing> getWingFromDatabase(String wingId) {
        return this.wingRepository.findById(wingId);
    }

    /**
     * Get all wings out of the database and returns it as a list of wings
     *
     * @return A list of wings out of the database
     * @author Tim de Kok
     */
    public List<Wing> getAllWingsFromDatabase() {
        return this.wingRepository.findAll();
    }

    /**
     * Save a new wing to the database
     *
     * @param wing The wing what needs to be safed in the database
     * @author Tim de Kok
     */
    public Wing saveWingToDatabase(Wing wing) {
        return this.wingRepository.save(wing);
    }

    /**
     * Update a existing wing in the database
     *
     * @param id The id of the wing what needs to be update
     * @param wingUpdate The updated version of the wing
     * @author Tim de Kok
     */
    public Wing updateWingInDatabase(String id, WingDTO wingUpdate) throws EntryNotFoundException {
        final Optional<Wing> byId = this.wingRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        final Wing wing = this.wingMapper.mergeWing(byId.get(), wingUpdate);

        return this.wingRepository.saveAndFlush(wing);
    }

    /**
     * Removes a wing out of the database
     *
     * @param wingId Id of the wing what needs to be removed
     * @author Tim de Kok
     */
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
