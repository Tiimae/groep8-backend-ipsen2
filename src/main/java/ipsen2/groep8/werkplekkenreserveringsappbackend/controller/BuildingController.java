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

@Controller
@RequestMapping(value = "/api/building")
public class BuildingController {

    private final BuildingDAO buildingDAO;
    private BuildingMapper buildingMapper;

    public BuildingController(BuildingDAO buildingDAO, BuildingMapper buildingMapper) {
        this.buildingDAO = buildingDAO;
        this.buildingMapper = buildingMapper;
    }

    @RequestMapping(value = "/{buildingid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Optional<Building>> getBuilding(@PathVariable String buildingid) {
        final Optional<Building> building = this.buildingDAO.getBuildingFromDatabase(buildingid);
        if (building.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Dit gebouw bestaat niet");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, building);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<Building>> getBuildings() {
        final List<Building> allBuildingsFromDatabase = this.buildingDAO.getAllBuildingsFromDatabase();
        return new ApiResponse(HttpStatus.ACCEPTED, allBuildingsFromDatabase);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postBuilding(@RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.saveBuildingToDatabase(building);
        return new ApiResponse(HttpStatus.CREATED, building);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ApiResponse updateBuilding(@PathVariable String id, @RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingMapper.toBuilding(buildingDTO);
        this.buildingDAO.updateBuildingInDatabase(id, building);
        return new ApiResponse(HttpStatus.ACCEPTED, building);
    }

    @DeleteMapping(value = "/{buildingid}")
    @ResponseBody
    public ApiResponse deleteBuilding(@PathVariable String buildingid) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingid);
        return new ApiResponse(HttpStatus.ACCEPTED, "Building has been deleted");
    }
}
