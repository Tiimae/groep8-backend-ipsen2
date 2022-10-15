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

    @RequestMapping(value = "/{wingId}/meeting-room/{meetingRoomId}/attach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse attachMeetingRoomToWing(@PathVariable String wingId, @PathVariable String meetingRoomId) {
        this.wingDAO.attachMeetingRoomToWingInDatabase(wingId, meetingRoomId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been attached to a meeting room");
    }

    @RequestMapping(value = "/{wingId}/meeting-room/{meetingRoomId}/detach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse detachMeetingRoomToWing(@PathVariable String wingId, @PathVariable String meetingRoomId) {
        this.wingDAO.detachMeetingRoomToWingInDatabase(wingId, meetingRoomId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been detached to a meeting room");
    }

    @RequestMapping(value = "/{wingId}/department/{departmentId}/attach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse attachWingToDeparment(@PathVariable String departmentId, @PathVariable String wingId) {
        this.wingDAO.attachWingToDepartmentInDatabase(wingId, departmentId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been attached to the department");
    }

    @RequestMapping(value = "/{wingId}/department/{departmentId}/detach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse detachWingToDeparment(@PathVariable String wingId, @PathVariable String departmentId) {
        this.wingDAO.detachWingFromDepartmentInDatabase(wingId, departmentId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Wing has been detached to the department");
    }
}
