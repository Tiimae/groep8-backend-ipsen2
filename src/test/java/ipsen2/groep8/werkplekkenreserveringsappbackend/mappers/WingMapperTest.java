package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WingMapperTest {

    private WingMapper wingMapper;

    @Mock private BuildingDAO buildingDAO;
    @Mock private DepartmentDAO departmentDAO;
    @Mock private MeetingRoomDAO meetingRoomDAO;
    @Mock private ReservationDAO reservationDAO;

    @Before
    public void setup() {
        this.wingMapper = new WingMapper(this.reservationDAO, this.buildingDAO, this.departmentDAO, this.meetingRoomDAO);
    }

    @Test
    public void should_returnauser_when_touserfunctionhasbeencalled() throws EntryNotFoundException {

        //arrange
        final Building building = new Building();
        building.setId("1");

        final Department department1 = new Department("test1", new HashSet<>(), new HashSet<>());
        final Department department2 = new Department("test2", new HashSet<>(), new HashSet<>());
        final Set<Department> departments = new HashSet<>();

        department1.setId("1");
        department2.setId("2");

        departments.add(department1);
        departments.add(department2);

        final MeetingRoom meetingRoom1 = new MeetingRoom(null, new Wing(), new HashSet<>());
        final MeetingRoom meetingRoom2 = new MeetingRoom(null, new Wing(), new HashSet<>());
        final Set<MeetingRoom> meetingRooms = new HashSet<>();

        meetingRoom1.setId("1");
        meetingRoom2.setId("2");

        meetingRooms.add(meetingRoom1);
        meetingRooms.add(meetingRoom2);

        final Reservation reservation1 = new Reservation(null, null, false, 0, "", null, new HashSet<>(), null, null);
        final Reservation reservation2 = new Reservation(null, null, false, 0, "", null, new HashSet<>(), null, null);
        final Set<Reservation> reservations = new HashSet<>();

        reservation1.setId("1");
        reservation2.setId("2");

        reservations.add(reservation1);
        reservations.add(reservation2);

        final WingDTO wingDTO = new WingDTO();
        wingDTO.setName("test");
        wingDTO.setFloor(null);
        wingDTO.setWorkplaces(null);
        wingDTO.setBuildingId(building.getId());
        wingDTO.setDepartmentIds(new String[] {department1.getId(), department2.getId()});
        wingDTO.setReservationIds(new String[] {reservation1.getId(), reservation2.getId()});
        wingDTO.setMeetingRoomIds(new String[] {meetingRoom1.getId(), meetingRoom2.getId()});

        final Wing expectedWing = new Wing("test", null, null, departments, building, meetingRooms, reservations);

        when(this.buildingDAO.getBuildingFromDatabase(building.getId())).thenReturn(Optional.of(building));
        when(this.departmentDAO.getDepartmentFromDatabase(department1.getId())).thenReturn(Optional.of(department1));
        when(this.departmentDAO.getDepartmentFromDatabase(department2.getId())).thenReturn(Optional.of(department2));
        when(this.reservationDAO.getReservationFromDatabase(reservation1.getId())).thenReturn(Optional.of(reservation1));
        when(this.reservationDAO.getReservationFromDatabase(reservation2.getId())).thenReturn(Optional.of(reservation2));
        when(this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoom1.getId())).thenReturn(Optional.of(meetingRoom1));
        when(this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoom2.getId())).thenReturn(Optional.of(meetingRoom2));

        //act
        final Wing actualWing = this.wingMapper.toWing(wingDTO);

        //assign
        assertEquals(expectedWing.getName(), actualWing.getName());
        assertEquals(expectedWing.getFloor(), actualWing.getFloor());
        assertEquals(expectedWing.getReservations(), actualWing.getReservations());
        assertEquals(expectedWing.getMeetingRooms(), actualWing.getMeetingRooms());
        assertEquals(expectedWing.getDepartments(), actualWing.getDepartments());
        assertEquals(expectedWing.getBuilding(), actualWing.getBuilding());
        assertEquals(expectedWing.getWorkplaces(), actualWing.getWorkplaces());
    }

    @Test
    public void should_returnupdatedwing_when_mergewingmethodhasbeencalled() {

        //Assert
        final Wing wing = new Wing("test", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
        final Wing expectedWing = new Wing("test1", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());

        //Act
        final Wing actualWing = this.wingMapper.mergeWing(wing, expectedWing);

        //Assign
        assertEquals(expectedWing.getName(), actualWing.getName());
    }
}
