package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.BuildingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BuildingDAO {
    private BuildingRepository buildingRepository;
    private WingRepository wingRepository;

    public BuildingDAO(BuildingRepository buildingRepository, WingRepository wingRepository) {
        this.buildingRepository = buildingRepository;
        this.wingRepository = wingRepository;
    }

    public Optional<Building> getBuildingFromDatabase(String buildingid) {
        return this.buildingRepository.findById(buildingid);
    }

    public List<Building> getAllBuildingsFromDatabase() {
        return this.buildingRepository.findAll();
    }

    public void saveBuildingToDatabase(Building building) {
        this.buildingRepository.save(building);
    }

    public void updateBuildingInDatabase(Building building) {
        this.buildingRepository.save(building);
    }

    public void deleteBuildingFromDatabase(String buildingid) {
        this.buildingRepository.deleteById(buildingid);
    }

    public void attachBuildingToWingInDatabase(String buildingid, String wingid) {
        Wing wing = this.wingRepository.findById(wingid).get();
        Building building = this.buildingRepository.findById(buildingid).get();

        wing.setBuilding(building);
        this.wingRepository.save(wing);
    }

    public void detachBuildingToWingInDatabase(String buildingid, String wingid) {
        Wing wing = this.wingRepository.findById(wingid).get();
        Building building = this.buildingRepository.findById(buildingid).get();

        building.getWings().remove(wing);
        wing.setBuilding(null);

        this.wingRepository.save(wing);
        this.buildingRepository.save(building);
    }
}
