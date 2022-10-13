package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/meetingroom")
public class MeetingRoomController {
    private final MeetingRoomDAO meetingRoomDAO;

    public MeetingRoomController(MeetingRoomDAO meetingRoomDAO) {
        this.meetingRoomDAO = meetingRoomDAO;
    }

    @GetMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponse<Optional<MeetingRoom>> getMeetingRoom(@PathVariable String meetingRoomId) {
        Optional<MeetingRoom> meetingRoom = this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoomId);

        if (meetingRoom.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Meeting room has not been found");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, meetingRoom);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponse<List<MeetingRoom>> getMeetingRooms() {
        List<MeetingRoom> allMeetingRooms = this.meetingRoomDAO.getAllMeetingRoomsFromDatabase();

        return new ApiResponse(HttpStatus.ACCEPTED, allMeetingRooms);
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        this.meetingRoomDAO.saveMeetingRoomToDatabase(meetingRoom);
        return new ApiResponse(HttpStatus.CREATED, "MeetingRoom has been posted to the database");
    }

    @PutMapping(value = "", consumes = {"apllication/json"})
    @ResponseBody
    public ApiResponse updateUser(@RequestBody MeetingRoom meetingRoom) {
        this.meetingRoomDAO.updateMeetingRoomInDatabase(meetingRoom);
        return new ApiResponse(HttpStatus.ACCEPTED, "MeetingRoom has been updated");
    }

    @DeleteMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponse deleteUser(@PathVariable String meetingRoomId) {
        this.meetingRoomDAO.deleteMeetingRoomFromDatabase(meetingRoomId);
        return new ApiResponse(HttpStatus.ACCEPTED, "MeetingRoom has been deleted");
    }
}
