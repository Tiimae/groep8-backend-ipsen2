package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.MeetingRoomRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.MeetingRoomDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.MeetingRoomMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MeetingRoomDAO {
    /**
     * This is the variable for the MeetingRoomRepository in the class
     */
    private MeetingRoomRepository meetingRoomRepository;

    private MeetingRoomMapper meetingRoomMapper;

    /**
     * This is the constructor of the MeetingRoomDAO. It set the meetingRoomRepository
     *
     * @param meetingRoomRepository The repository for meeting room
     * @author Tim de Kok
     */
    public MeetingRoomDAO(MeetingRoomRepository meetingRoomRepository, MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.meetingRoomMapper = meetingRoomMapper;
    }

    /**
     * Get a specific meeting room out of the database by id and returns that meeting room
     *
     * @param meetingRoomId The id of the meeting room you want to have
     * @return A meeting room out of the database
     * @author Tim de Kok
     */
    public Optional<MeetingRoom> getMeetingRoomFromDatabase(String meetingRoomId) {
        return this.meetingRoomRepository.findById(meetingRoomId);
    }

    /**
     * Get all meeting rooms out of the database and returns it as a list of meeting rooms
     *
     * @return A list of meeting rooms out of the database
     * @author Tim de Kok
     */
    public List<MeetingRoom> getAllMeetingRoomsFromDatabase() {
        return this.meetingRoomRepository.findAll();
    }

    /**
     * Save a new meeting room to the database
     *
     * @param meetingRoom The meeting room what needs to be safed in the database
     * @author Tim de Kok
     */
    public void saveMeetingRoomToDatabase(MeetingRoom meetingRoom) {
        this.meetingRoomRepository.save(meetingRoom);
    }

    /**
     * Update a existing meeting room in the database
     *
     * @param id  Id of the meeting room that needs to be updated
     * @param meetingRoomdto The updated version of the meeting room
     * @author Tim de Kok
     */
    public MeetingRoom updateMeetingRoomInDatabase(String id, MeetingRoomDTO meetingRoomdto) throws EntryNotFoundException {
        final Optional<MeetingRoom> byId = this.meetingRoomRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        final MeetingRoom meetingRoom = this.meetingRoomMapper.mergeMeetingRoom(byId.get(), meetingRoomdto);

        return this.meetingRoomRepository.saveAndFlush(meetingRoom);
    }

    /**
     * Removes a meeting room out of the database
     *
     * @param meetingRoomId Id of the meeting room what needs to be removed
     * @author Tim de Kok
     */
    public void deleteMeetingRoomFromDatabase(String meetingRoomId) {
        final MeetingRoom meetingRoom = this.meetingRoomRepository.getById(meetingRoomId);

        meetingRoom.getWing().getMeetingRooms().remove(meetingRoom);
        meetingRoom.setWing(null);

        this.meetingRoomRepository.deleteById(meetingRoomId);
    }

}
