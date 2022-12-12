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
        Building building = this.getBuilding(wingDTO.getBuildingId());
        Set<Department> departments = this.getAllDepartments(wingDTO.getDepartmentIds());
        Set<Reservation> reservations = this.getAllReservations(wingDTO.getReservationIds());
        Set<MeetingRoom> meetingRooms = this.getAllMeetingRooms(wingDTO.getMeetingRoomIds());

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
    public Wing mergeWing(Wing base, WingDTO update) throws EntryNotFoundException {
        base.setName(update.getName());
        base.setWorkplaces(update.getWorkplaces());
        base.setFloor(update.getFloor());

        for (Department department : this.getAllDepartments(update.getDepartmentIds())) {
            if (!base.getDepartments().contains(department)) {
                base.addDepartment(department);
            }
        }

        for (MeetingRoom meetingRoom : this.getAllMeetingRooms(update.getMeetingRoomIds())) {
            if (!base.getMeetingRooms().contains(meetingRoom)) {
                base.addMeetingRoom(meetingRoom);
            }
        }

        if (this.getBuilding(update.getBuildingId()) != null) {
            base.setBuilding(this.getBuilding(update.getBuildingId()));
        }

        return base;
    }

    public Set<Reservation> getAllReservations(String[] reservationIds) {
        Set<Reservation> reservations = new HashSet<>();
        if (reservationIds != null) {
            reservations = Arrays.stream(reservationIds)
                    .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return reservations;
    }

    public Set<Department> getAllDepartments(String[] departmentIds) {
        Set<Department> departments = new HashSet<>();
        if (departmentIds != null) {
            departments = Arrays.stream(departmentIds)
                    .map(id -> this.departmentDAO.getDepartmentFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return departments;
    }

    public Set<MeetingRoom> getAllMeetingRooms(String[] meetingRoomIds) {
        Set<MeetingRoom> meetingRooms = new HashSet<>();
        if (meetingRoomIds != null) {
            meetingRooms = Arrays.stream(meetingRoomIds)
                    .map(id -> this.meetingRoomDAO.getMeetingRoomFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return meetingRooms;
    }

    public Building getBuilding(String buildingId) throws EntryNotFoundException {
        Building building = null;

        if (buildingId != null) {
            final Optional<Building> buildingFromDatabase = this.buildingDAO.getBuildingFromDatabase(buildingId);

            if (buildingFromDatabase.isEmpty()) {
                throw new EntryNotFoundException("Building not found");
            }

            building = buildingFromDatabase.get();
        }

        return building;
    }
}
