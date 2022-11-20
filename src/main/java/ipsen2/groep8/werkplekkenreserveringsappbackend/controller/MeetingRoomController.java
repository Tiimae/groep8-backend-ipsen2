package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.MeetingRoomDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.MeetingRoomMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/meeting-room")
public class MeetingRoomController {
    private final MeetingRoomDAO meetingRoomDAO;
    private final MeetingRoomMapper meetingRoomMapper;

    public MeetingRoomController(MeetingRoomDAO meetingRoomDAO, MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomDAO = meetingRoomDAO;
        this.meetingRoomMapper = meetingRoomMapper;
    }

    @GetMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponseService<Optional<MeetingRoom>> getMeetingRoom(@PathVariable String meetingRoomId) {
        Optional<MeetingRoom> meetingRoom = this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoomId);

        if (meetingRoom.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Meeting room has not been found");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, meetingRoom);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponseService<List<MeetingRoom>> getMeetingRooms() {
        List<MeetingRoom> allMeetingRooms = this.meetingRoomDAO.getAllMeetingRoomsFromDatabase();

        return new ApiResponseService(HttpStatus.ACCEPTED, allMeetingRooms);
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService postMeetingRoom(@RequestBody @Valid MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {

        this.meetingRoomDAO.saveMeetingRoomToDatabase(this.meetingRoomMapper.toMeetingRoom(meetingRoomDTO));
        return new ApiResponseService(HttpStatus.CREATED, "MeetingRoom has been posted to the database");
    }

    @PutMapping(value = "/{id}", consumes = {"apllication/json"})
    @ResponseBody
    public ApiResponseService updateUser(@PathVariable String id, @RequestBody @Valid MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {
        final MeetingRoom meetingRoomUpdate = this.meetingRoomMapper.toMeetingRoom(meetingRoomDTO);
        MeetingRoom meetingRoom = this.meetingRoomDAO.getMeetingRoomFromDatabase(id).get();

        meetingRoom = this.meetingRoomMapper.mergeMeetingRoom(meetingRoom, meetingRoomUpdate);

        this.meetingRoomDAO.updateMeetingRoomInDatabase(meetingRoom);
        return new ApiResponseService(HttpStatus.ACCEPTED, "MeetingRoom has been updated");
    }

    @DeleteMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponseService deleteUser(@PathVariable String meetingRoomId) {
        this.meetingRoomDAO.deleteMeetingRoomFromDatabase(meetingRoomId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "MeetingRoom has been deleted");
    }
}
