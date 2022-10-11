package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
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
    public Optional<Building> getBuilding(@PathVariable String buildingid) {
        return this.buildingDAO.getBuildingFromDatabase(buildingid);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Building> getBuildings() {
        return this.buildingDAO.getAllBuildingsFromDatabase();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public String postBuilding(@RequestBody Building building) {
        this.buildingDAO.saveBuildingToDatabase(building);
        return "Building has been posted to the database";
    }

    @PutMapping(value = "")
    @ResponseBody
    public String updateBuilding(@RequestBody Building building) {
        this.buildingDAO.updateBuildingInDatabase(building);
        return "User has been updated";
    }

    @DeleteMapping(value = "/{buildingid}")
    @ResponseBody
    public String deleteBuilding(@PathVariable String buildingid) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingid);
        return "Building has been deleted";
    }
}
