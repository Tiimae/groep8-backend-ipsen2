package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.MeetingRoomRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WingDAO {
    private WingRepository wingRepository;
    private MeetingRoomRepository meetingRoomRepository;

    public WingDAO(WingRepository wingRepository, MeetingRoomRepository meetingRoomRepository) {
        this.wingRepository = wingRepository;
        this.meetingRoomRepository = meetingRoomRepository;
    }

    public Optional<Wing> getWingFromDatabase(String wingId) {
        return this.wingRepository.findById(wingId);
    }

    public List<Wing> getAllWingsFromDatabase() {
        return this.wingRepository.findAll();
    }

    public void saveWingToDatabase(Wing wing) {
        this.wingRepository.save(wing);
    }

    public void updateWingInDatabase(Wing wing) {
        this.wingRepository.save(wing);
    }

    public void deleteWingFromDatabase(String wingId) {
        this.wingRepository.deleteById(wingId);
    }

    public void attachMeetingRoomToWingInDatabase(String wingId, String meetingRoomId) {
        final MeetingRoom meetingRoom = this.meetingRoomRepository.findById(meetingRoomId).get();
        final Wing wing = this.wingRepository.findById(wingId).get();

        wing.getMeetingRooms().add(meetingRoom);

        this.wingRepository.save(wing);
    }

    public void detachMeetingRoomToWingInDatabase(String wingId, String meetingRoomId) {
        final MeetingRoom meetingRoom = this.meetingRoomRepository.findById(meetingRoomId).get();
        final Wing wing = this.wingRepository.findById(wingId).get();

        wing.getMeetingRooms().remove(meetingRoom);
        meetingRoom.setWing(null);

        this.wingRepository.save(wing);
        this.meetingRoomRepository.save(meetingRoom);
    }
}
