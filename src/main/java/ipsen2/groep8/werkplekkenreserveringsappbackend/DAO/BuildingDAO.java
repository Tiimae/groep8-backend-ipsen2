package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.BuildingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.context.annotation.Lazy;
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

    public Building saveBuildingToDatabase(Building building) {
        return this.buildingRepository.save(building);
    }

    public Building updateBuildingInDatabase(Building updatedBuilding) {
        return this.buildingRepository.saveAndFlush(updatedBuilding);
    }

    public void deleteBuildingFromDatabase(String buildingid) {
        this.buildingRepository.deleteById(buildingid);
    }
}
