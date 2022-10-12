package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
