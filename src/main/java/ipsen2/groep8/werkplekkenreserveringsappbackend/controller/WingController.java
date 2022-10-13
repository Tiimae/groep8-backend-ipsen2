package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/wing")
public class WingController {
    private final WingDAO wingDAO;

    public WingController(WingDAO wingDAO) {
        this.wingDAO = wingDAO;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ApiResponse<Optional<Wing>> getWing(@PathVariable String id) {
        Optional<Wing> wing = this.wingDAO.getWingFromDatabase(id);

        if (wing.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "The wing has not been found!");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, wing);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponse<List<Wing>> getWings() {
        List<Wing> allWings = this.wingDAO.getAllWingsFromDatabase();

        return new ApiResponse(HttpStatus.ACCEPTED, allWings);
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postWing(@RequestBody Wing wing) {
        this.wingDAO.saveWingToDatabase(wing);

        return new ApiResponse(HttpStatus.CREATED, "The wing has been posted!");
    }

    @PutMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse updateWing(@RequestBody Wing wing) {
        this.wingDAO.updateWingInDatabase(wing);
        return new ApiResponse(HttpStatus.ACCEPTED, "The wing has been updated!");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ApiResponse deleteWing(@PathVariable String id) {
        this.wingDAO.deleteWingFromDatabase(id);

        return new ApiResponse(HttpStatus.ACCEPTED, "The wing has been deleted");
    }
}
