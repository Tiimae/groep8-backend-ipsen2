package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/building")
public class BuildingController {

    private final BuildingDAO buildingDAO;

    public BuildingController(BuildingDAO buildingDAO) {
        this.buildingDAO = buildingDAO;
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
    public ApiResponse postBuilding(@RequestBody Building building) {
        this.buildingDAO.saveBuildingToDatabase(building);
        return new ApiResponse(HttpStatus.CREATED, "Building has been posted to the database");
    }

    @PutMapping(value = "")
    @ResponseBody
    public ApiResponse updateBuilding(@RequestBody Building building) {
        this.buildingDAO.updateBuildingInDatabase(building);
        return new ApiResponse(HttpStatus.ACCEPTED, "User has been updated");
    }

    @DeleteMapping(value = "/{buildingid}")
    @ResponseBody
    public ApiResponse deleteBuilding(@PathVariable String buildingid) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingid);
        return new ApiResponse(HttpStatus.ACCEPTED, "Building has been deleted");
    }

    @RequestMapping(value = "/{buildingid}/wing/{wingid}/attach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse attachBuildingFromWing(@PathVariable String buildingid, @PathVariable String wingid) {
        this.buildingDAO.attachBuildingToWingInDatabase(buildingid, wingid);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been added from the building");
    }

    @RequestMapping(value = "/{buildingid}/wing/{wingid}/detach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse detachBuildingFromWing(@PathVariable String buildingid, @PathVariable String wingid) {
        this.buildingDAO.detachBuildingToWingInDatabase(buildingid, wingid);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been removed from the building");
    }
}
