package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/api/building")
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
     * This is the consturctor of the BuildingController. It set the BuildingDAO and the BuildingMapper
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
     * @param buildingid The building id what we get from the url
     * @return an ApiResponse with a statuscode and a building
     * @author Tim de Kok
     */
    @RequestMapping(value = "/{buildingid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Optional<Building>> getBuilding(@PathVariable String buildingid) {
        final Optional<Building> building = this.buildingDAO.getBuildingFromDatabase(buildingid);
        if (building.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Dit gebouw bestaat niet");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, building);
    }

    /**
     * This function get all the buildings in the database and returns all the building as a List
     *
     * @return an ApiResponse with a statuscode and a list of all buildings
     * @author Tim de Kok
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<Building>> getBuildings() {
        final List<Building> allBuildingsFromDatabase = this.buildingDAO.getAllBuildingsFromDatabase();
        return new ApiResponse(HttpStatus.ACCEPTED, allBuildingsFromDatabase);
    }


    /**
     * This function creates an new building in the database and return the specific building back
     *
     * @param buildingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and  the building what just got created
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postBuilding(@RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.saveBuildingToDatabase(building);
        return new ApiResponse(HttpStatus.CREATED, building);
    }

    /**
     * This function updates an building and returns the building what just got updated back
     *
     * @param id          This is the building id that passed into the url
     * @param buildingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and  the building what just got updated
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse updateBuilding(@PathVariable String id, @RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.updateBuildingInDatabase(id, building);
        return new ApiResponse(HttpStatus.ACCEPTED, building);
    }

    /**
     * This function removes an building from the database and send an Api response back
     *
     * @param buildingid The building id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = "/{buildingid}")
    @ResponseBody
    public ApiResponse deleteBuilding(@PathVariable String buildingid) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingid);
        return new ApiResponse(HttpStatus.ACCEPTED, "Building has been deleted");
    }
}
