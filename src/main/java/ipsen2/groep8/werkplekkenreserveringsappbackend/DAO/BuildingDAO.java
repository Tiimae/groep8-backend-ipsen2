package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.BuildingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Component
public class BuildingDAO {

    /**
     * This is the variable for the BuildingRepository in the class
     */
    private BuildingRepository buildingRepository;

    /**
     * This is the variable for the BuildingMapper in the class
     */
    private BuildingMapper buildingMapper;

    /**
     * This is the constructor of the BuildingDAO. It set the buildingMapper and buildingRepository
     *
     * @param buildingMapper The Mapper for building
     * @param buildingRepository The repository for building
     * @author Tim de Kok
     */
    public BuildingDAO(BuildingRepository buildingRepository, @Lazy BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    /**
     * Get a specific building out of the database by id and returns that building
     *
     * @param buildingid The id of the building you want to have
     * @return A building out of the database
     * @author Tim de Kok
     */
    public Optional<Building> getBuildingFromDatabase(String buildingid) {
        return this.buildingRepository.findById(buildingid);
    }

    /**
     * Get all buildings out of the database and returns it as a list of buildings
     *
     * @return A list of buildings out of the database
     * @author Tim de Kok
     */
    public List<Building> getAllBuildingsFromDatabase() {
        return this.buildingRepository.findAll();
    }

    /**
     * Save a new building to the database
     *
     * @param building The building what needs to be safed in the database
     * @author Tim de Kok
     */
    public void saveBuildingToDatabase(Building building) {
        this.buildingRepository.save(building);
    }

    /**
     * Update a existing building in the database
     *
     * @param id The id of the building what needs to be update
     * @param buildingUpdate The updated version of the building
     * @author Tim de Kok
     */
    public void updateBuildingInDatabase(String id, Building buildingUpdate) {
        Building building = this.buildingRepository.getById(id);
        building = this.buildingMapper.mergeBuilding(building, buildingUpdate);
        this.buildingRepository.save(building);
    }

    /**
     * Removes a building out of the database
     *
     * @param buildingid Id of the building what needs to be removed
     * @author Tim de Kok
     */
    public void deleteBuildingFromDatabase(String buildingid) {
        this.buildingRepository.deleteById(buildingid);
    }
}
