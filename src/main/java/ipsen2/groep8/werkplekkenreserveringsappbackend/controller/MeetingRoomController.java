package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
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
    public Optional<MeetingRoom> getMeetingRoom(@PathVariable Long meetingRoomId) {
        return this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoomId);
    }

    @GetMapping(value = "")
    @ResponseBody
    public List<MeetingRoom> getMeetingRooms() {
        return this.meetingRoomDAO.getAllMeetingRoomsFromDatabase();
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String postMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        this.meetingRoomDAO.saveMeetingRoomToDatabase(meetingRoom);
        return "MeetingRoom has been posted to the database";
    }

    @PutMapping(value = "", consumes = {"apllication/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUser(@RequestBody MeetingRoom meetingRoom) {
        this.meetingRoomDAO.updateMeetingRoomInDatabase(meetingRoom);
        return "MeetingRoom has been updated";
    }

    @DeleteMapping(value = "/{meetingRoomId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteUser(@PathVariable Long meetingRoomId) {
        this.meetingRoomDAO.deleteMeetingRoomFromDatabase(meetingRoomId);
        return "MeetingRoom has been deleted";
    }
}
