package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
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

    @GetMapping(value = "/{buildingid}")
    @ResponseBody
    public ApiResponseService<Optional<Building>> getBuilding(@PathVariable String buildingid) {
        final Optional<Building> building = this.buildingDAO.getBuildingFromDatabase(buildingid);
        if (building.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.NOT_FOUND, "This building doesn't exist");
        }

        return new ApiResponseService<>(HttpStatus.FOUND, building);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponseService<List<Building>> getBuildings() {
        return new ApiResponseService<>(HttpStatus.ACCEPTED, this.buildingDAO.getAllBuildingsFromDatabase());
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Building> postBuilding(@RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Building building = this.buildingDAO.saveBuildingToDatabase(this.buildingMapper.toBuilding(buildingDTO));

        if (building == null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, "Building has not been added to the database.");
        }

        return new ApiResponseService<>(HttpStatus.CREATED, building);
    }

    @PutMapping(value = "/{buildingId}")
    @ResponseBody
    public ApiResponseService<Building> updateBuilding(@PathVariable String buildingId, @RequestBody @Valid BuildingDTO buildingDTO) throws EntryNotFoundException {
        final Optional<Building> buildingFromDatabase = this.buildingDAO.getBuildingFromDatabase(buildingId);

        if (buildingFromDatabase.isEmpty()) {
            this.postBuilding(buildingDTO);
        }

        final Building updatedBuilding = this.buildingMapper.mergeBuilding(buildingFromDatabase.get(), this.buildingMapper.toBuilding(buildingDTO));
        final Building building = this.buildingDAO.updateBuildingInDatabase(updatedBuilding);

        return new ApiResponseService<>(HttpStatus.ACCEPTED, building);
    }

    @DeleteMapping(value = "/{buildingId}")
    @ResponseBody
    public ApiResponseService deleteBuilding(@PathVariable String buildingId) {
        this.buildingDAO.deleteBuildingFromDatabase(buildingId);
        return new ApiResponseService<>(HttpStatus.ACCEPTED, "Building has been deleted");
    }
}
