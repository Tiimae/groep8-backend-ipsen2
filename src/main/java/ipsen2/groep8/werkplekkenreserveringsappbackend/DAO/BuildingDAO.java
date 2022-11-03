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
    private BuildingMapper buildingMapper;

    public BuildingDAO(BuildingRepository buildingRepository, @Lazy BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
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

    public void updateBuildingInDatabase(String id, Building buildingUpdate) {
        Building building = this.buildingRepository.getById(id);
        building = this.buildingMapper.mergeBuilding(building, buildingUpdate);
        this.buildingRepository.save(building);
    }

    public void deleteBuildingFromDatabase(String buildingid) {
        this.buildingRepository.deleteById(buildingid);
    }
}
