package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@RestController
@RequestMapping()
public class BuildingController {
    /**
     * This is the variable for the BuildingDAO in the class
     */
    private final BuildingDAO buildingDAO;
    /**
     * This is the variable for the BuildingMapper in the class
     */
    private BuildingMapper buildingMapper;

    /**
     * This is the constructor of the BuildingController. It set the BuildingDAO and the BuildingMapper
     *
     * @param buildingDAO The DAO for building
     * @param buildingMapper The mapper for building
     * @author Tim de Kok
     */
    public BuildingController(BuildingDAO buildingDAO, BuildingMapper buildingMapper) {
        this.buildingDAO = buildingDAO;
        this.buildingMapper = buildingMapper;
    }


    /**
     * This function returns an ApiResponse with a status code and a specific building what will be returned from the BuildingDAO
     *
     * @param buildingId The building id what we get from the url
     * @return an ApiResponse with a statuscode and a building
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getBuilding)
    @ResponseBody
    public ApiResponseService<Optional<Building>> getBuilding(@PathVariable String buildingId) {
        final Optional<Building> building = this.buildingDAO.getBuildingFromDatabase(buildingId);
        if (building.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Dit gebouw bestaat niet");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, building);
    }

    /**
     * This function get all the buildings in the database and returns all the building as a List
     *
     * @return an ApiResponse with a statuscode and a list of all buildings
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getAllBuildings)
    @ResponseBody
    public ApiResponseService<List<Building>> getBuildings() {
        final List<Building> allBuildingsFromDatabase = this.buildingDAO.getAllBuildingsFromDatabase();
        return new ApiResponseService(HttpStatus.ACCEPTED, allBuildingsFromDatabase);
    }


    /**
     * This function creates an new building in the database and return the specific building back
     *
     * @param buildingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and  the building what just got created
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PostMapping(value = ApiConstant.getAllBuildings, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService postBuilding(@RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.saveBuildingToDatabase(building);
        return new ApiResponseService(HttpStatus.CREATED, building);
    }

    /**
     * This function updates an building and returns the building what just got updated back
     *
     * @param buildingId          This is the building id that passed into the url
     * @param buildingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and  the building what just got updated
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PutMapping(value = ApiConstant.getBuilding, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateBuilding(@PathVariable String buildingId, @RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.updateBuildingInDatabase(buildingId, building);
        return new ApiResponseService(HttpStatus.ACCEPTED, building);
    }

    /**
     * This function removes an building from the database and send an Api response back
     *
     * @param buildingId The building id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = ApiConstant.getBuilding)
    @ResponseBody
    public ApiResponseService deleteBuilding(@PathVariable String buildingId) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Building has been deleted");
    }
}
