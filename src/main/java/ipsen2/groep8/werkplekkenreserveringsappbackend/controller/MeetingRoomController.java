package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.MeetingRoomDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.MeetingRoomMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
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
@RequestMapping(value = "/api/meeting-room")
public class MeetingRoomController {

    /**
     * This is the variable for the MeetingRoomDAO in the class
     */
    private final MeetingRoomDAO meetingRoomDAO;

    /**
     * This is the variable for the MeetingRoomMapper in the class
     */
    private final MeetingRoomMapper meetingRoomMapper;

    /**
     * This is the constructor of the MeetingRoomController. It set the MeetingRoomDAO and the MeetingRoomMapper
     *
     * @param meetingRoomDAO The DAO for department
     * @param meetingRoomMapper The mapper for department
     * @author Tim de Kok
     */
    public MeetingRoomController(MeetingRoomDAO meetingRoomDAO, MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomDAO = meetingRoomDAO;
        this.meetingRoomMapper = meetingRoomMapper;
    }

    /**
     * This function returns an ApiResponse with a status code and a specific meeting room what will be returned from the MeetingRoomDAO
     *
     * @param meetingRoomId The meeting room id what we get from the url
     * @return an ApiResponse with a statuscode and a meeting room
     * @author Tim de Kok
     */
    @GetMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponseService<Optional<MeetingRoom>> getMeetingRoom(@PathVariable String meetingRoomId) {
        Optional<MeetingRoom> meetingRoom = this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoomId);

        if (meetingRoom.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Meeting room has not been found");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, meetingRoom);
    }

    /**
     * This function get all the meeting rooms in the database and returns all the meeting rooms as a List
     *
     * @return an ApiResponse with a statuscode and a list of all meeting rooms
     * @author Tim de Kok
     */
    @GetMapping(value = "")
    @ResponseBody
    public ApiResponseService<List<MeetingRoom>> getMeetingRooms() {
        List<MeetingRoom> allMeetingRooms = this.meetingRoomDAO.getAllMeetingRoomsFromDatabase();

        return new ApiResponseService(HttpStatus.ACCEPTED, allMeetingRooms);
    }

    /**
     * This function creates an new meeting room in the database and return the specific meeting room back
     *
     * @param meetingRoomDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the meeting room what just got created
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService postMeetingRoom(@RequestBody @Valid MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {
        MeetingRoom meetingRoom = this.meetingRoomMapper.toMeetingRoom(meetingRoomDTO);
        this.meetingRoomDAO.saveMeetingRoomToDatabase(this.meetingRoomMapper.toMeetingRoom(meetingRoomDTO));
        return new ApiResponseService(HttpStatus.CREATED, meetingRoom);
    }

    /**
     * This function updates an meeting room and returns the meeting room what just got updated back
     *
     * @param id          This is the meeting room id that passed into the url
     * @param meetingRoomDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the meeting room what just got updated
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateUser(@PathVariable String id, @RequestBody @Valid MeetingRoomDTO meetingRoomDTO) throws EntryNotFoundException {
        final MeetingRoom meetingRoomUpdate = this.meetingRoomMapper.toMeetingRoom(meetingRoomDTO);
        MeetingRoom meetingRoom = this.meetingRoomDAO.getMeetingRoomFromDatabase(id).get();

        meetingRoom = this.meetingRoomMapper.mergeMeetingRoom(meetingRoom, meetingRoomUpdate);

        this.meetingRoomDAO.updateMeetingRoomInDatabase(meetingRoom);
        return new ApiResponseService(HttpStatus.ACCEPTED, "MeetingRoom has been updated");
    }

    /**
     * This function removes an meeting room from the database and send an Api response back
     *
     * @param meetingRoomId The meeting room id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = "/{meetingRoomId}")
    @ResponseBody
    public ApiResponseService deleteUser(@PathVariable String meetingRoomId) {
        this.meetingRoomDAO.deleteMeetingRoomFromDatabase(meetingRoomId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "MeetingRoom has been deleted");
    }
}
