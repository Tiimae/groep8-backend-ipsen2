package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WingMapper {

    private ReservationDAO reservationDAO;
    private BuildingDAO buildingDAO;
    private DepartmentDAO departmentDAO;
    private MeetingRoomDAO meetingRoomDAO;

    public WingMapper(ReservationDAO reservationDAO, BuildingDAO buildingDAO, DepartmentDAO departmentDAO, MeetingRoomDAO meetingRoomDAO) {
        this.reservationDAO = reservationDAO;
        this.buildingDAO = buildingDAO;
        this.departmentDAO = departmentDAO;
        this.meetingRoomDAO = meetingRoomDAO;
    }

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
