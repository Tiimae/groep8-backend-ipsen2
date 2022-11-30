package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WingMapper {

    /**
     * This is the variable for the ReservationDAO in the class
     */
    private ReservationDAO reservationDAO;

    /**
     * This is the variable for the BuildingDAO in the class
     */
    private BuildingDAO buildingDAO;

    /**
     * This is the variable for the DepartmentDAO in the class
     */
    private DepartmentDAO departmentDAO;

    /**
     * This is the variable for the MeetingRoomDAO in the class
     */
    private MeetingRoomDAO meetingRoomDAO;

    /**
     * This is the constructor of the BuildingMapper. It set the ReservationDAO, BuildingDAO, DepartmetnDAO and MeetingRoomDAO
     *
     * @param reservationDAO The DAO for reservation
     * @param buildingDAO The DAO for building
     * @param departmentDAO The DAO for department
     * @param meetingRoomDAO The DAO for meeting room
     * @author Tim de Kok
     */

    public WingMapper(ReservationDAO reservationDAO, BuildingDAO buildingDAO, DepartmentDAO departmentDAO, MeetingRoomDAO meetingRoomDAO) {
        this.reservationDAO = reservationDAO;
        this.buildingDAO = buildingDAO;
        this.departmentDAO = departmentDAO;
        this.meetingRoomDAO = meetingRoomDAO;
    }

    /**
     * This function will be used to create a new wing in the database
     *
     * @param wingDTO The building data to create a new Wing
     * @return a new Wing
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    public Wing toWing(WingDTO wingDTO) throws EntryNotFoundException {
        String name = wingDTO.getName();
        Long workplaces = wingDTO.getWorkplaces();
        Long floor = wingDTO.getFloor();
        Building building = null;
        Set<Department> departments = new HashSet<>();
        Set<Reservation> reservations = new HashSet<>();
        Set<MeetingRoom> meetingRooms = new HashSet<>();

        if (wingDTO.getBuildingId() != null) {
            final Optional<Building> buildingFromDatabase = this.buildingDAO.getBuildingFromDatabase(wingDTO.getBuildingId());

            if (buildingFromDatabase.isEmpty()) {
                throw new EntryNotFoundException("Building not found");
            }

            building = buildingFromDatabase.get();
        }

        departments = Arrays.stream(wingDTO.getDepartmentIds())
                .map(id -> this.departmentDAO.getDepartmentFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        reservations = Arrays.stream(wingDTO.getReservationIds())
                .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        meetingRooms = Arrays.stream(wingDTO.getMeetingRoomIds())
                .map(id -> this.meetingRoomDAO.getMeetingRoomFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        return new Wing(name, workplaces, floor, departments, building, meetingRooms, reservations);
    }

    /**
     * This function returns an update wing, so we can save it in the database
     *
     * @param base The wing data that already exist in the database
     * @param update The wing data to create a new Wing
     * @return an updated wing
     * @author Tim de Kok
     */
    public Wing mergeWing(Wing base, Wing update) {
        base.setName(update.getName());
        base.setWorkplaces(update.getWorkplaces());
        base.setFloor(update.getFloor());
        base.setDepartments(update.getDepartments());
        base.setBuilding(update.getBuilding());
        base.setReservations(update.getReservations());
        base.setMeetingRooms(update.getMeetingRooms());

        return base;
    }
}
