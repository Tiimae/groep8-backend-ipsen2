package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.BuildingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BuildingDAO {
    private BuildingRepository buildingRepository;

    public BuildingDAO(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
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
}
