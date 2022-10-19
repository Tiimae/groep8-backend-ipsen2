package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.MeetingRoomRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MeetingRoomDAO {
    private MeetingRoomRepository meetingRoomRepository;

    public MeetingRoomDAO(MeetingRoomRepository meetingRoomRepository) {
        this.meetingRoomRepository = meetingRoomRepository;
    }

    public Optional<MeetingRoom> getMeetingRoomFromDatabase(String meetingRoomId) {
        return this.meetingRoomRepository.findById(meetingRoomId);
    }

    public List<MeetingRoom> getAllMeetingRoomsFromDatabase() {
        return this.meetingRoomRepository.findAll();
    }

    public void saveMeetingRoomToDatabase(MeetingRoom meetingRoom) {
        this.meetingRoomRepository.save(meetingRoom);
    }

    public void updateMeetingRoomInDatabase(MeetingRoom meetingRoom) {
        this.meetingRoomRepository.save(meetingRoom);
    }

    public void deleteMeetingRoomFromDatabase(String meetingRoomId) {
        this.meetingRoomRepository.deleteById(meetingRoomId);
    }

}
