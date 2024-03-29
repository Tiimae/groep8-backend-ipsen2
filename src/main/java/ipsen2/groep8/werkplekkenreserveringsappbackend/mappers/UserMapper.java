package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * This is the variable for the DepartmentDAO in the class
     */
    private DepartmentDAO departmentDAO;

    /**
     * This is the variable for the RoleDAO in the class
     */
    private RoleDAO roleDAO;

    /**
     * This is the variable for the ReservationDAO in the class
     */
    private ReservationDAO reservationDAO;

    /**
     * This is the constructor of the UserMapper. It set the DepartmentDAO, RoleDAO and ReservationDAO
     *
     * @param departmentDAO  The DAO for department
     * @param roleDAO        The DAO for role
     * @param reservationDAO The DAO for reservation
     * @author Tim de Kok
     */
    public UserMapper(DepartmentDAO departmentDAO, RoleDAO roleDAO, ReservationDAO reservationDAO) {
        this.departmentDAO = departmentDAO;
        this.roleDAO = roleDAO;
        this.reservationDAO = reservationDAO;
    }

    /**
     * This function will be used to create a new user in the database
     *
     * @param userDTO The user data to create a new user
     * @return a new user
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     * @author Tim de Kok
     */
    public User toUser(UserDTO userDTO) throws EntryNotFoundException {
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        Boolean verified = false;
        if (userDTO.getVerified() != null) {
            verified = userDTO.getVerified();
        }

        Boolean resetRequired = false;
        if (userDTO.getResetRequired() != null) {
            resetRequired = userDTO.getResetRequired();
        }

        Department department = this.getDepartment(userDTO.getDepartmentId());
        Set<Role> roles = this.getAllRoles(userDTO.getRoleIds());
        Set<Reservation> reservations = this.getAllReservations(userDTO.getReservationIds());

        return new User(name, email, password, verified, resetRequired, roles, department, reservations);
    }

    /**
     * This function returns an update user, so we can save it in the database
     *
     * @param base   The user data that already exist in the database
     * @param update The user data to create a new user
     * @return an updated user
     * @author Tim de Kok
     */
    public User mergeUser(User base, UserDTO update) throws EntryNotFoundException {
        base.setName(update.getName());
        base.setEmail(update.getEmail());

        if (!Objects.equals(update.getPassword(), "")) {
            base.setPassword(update.getPassword());
        }

        base.setVerified(base.getVerified());
        base.setReset_required(base.getReset_required());

        if (update.getDepartmentId() != null) {
            base.setDepartment(this.getDepartment(update.getDepartmentId()));
        }

        base.removeAllRoles();

        for (Role role : this.getAllRoles(update.getRoleIds())) {
            base.addRoles(role);
        }

        return base;
    }

    public Set<Role> getAllRoles(String[] roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            roles = Arrays.stream(roleIds)
                    .map(id -> this.roleDAO.getRoleFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return roles;
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

    public Department getDepartment(String departmentId) throws EntryNotFoundException {
        Department department = null;

        if (departmentId != null) {
            final Optional<Department> departmentEntry = departmentDAO.getDepartmentFromDatabase(departmentId);
            if (departmentEntry.isEmpty()) {
                throw new EntryNotFoundException("Department not found.");
            }

            department = departmentEntry.get();
        }

        return department;
    }

}
