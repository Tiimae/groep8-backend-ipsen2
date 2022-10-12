package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WingDAO {
    private WingRepository wingRepository;

    public WingDAO(WingRepository wingRepository) {
        this.wingRepository = wingRepository;
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

    public void updateWingInDatabase(Wing wing) {
        this.wingRepository.save(wing);
    }

    public void deleteWingFromDatabase(String wingId) {
        this.wingRepository.deleteById(wingId);
    }
}
