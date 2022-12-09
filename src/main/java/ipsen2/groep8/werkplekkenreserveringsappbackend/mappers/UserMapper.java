package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
     * @param departmentDAO The DAO for department
     * @param roleDAO The DAO for role
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
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    public User toUser(UserDTO userDTO) throws EntryNotFoundException {
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        Boolean verified = false;
        if(userDTO.getVerified() != null){
            verified = userDTO.getVerified();
        }

        Department department = null;
        if (userDTO.getDepartmentId() != null) {
            final Optional<Department> departmentEntry = departmentDAO.getDepartmentFromDatabase(userDTO.getDepartmentId());
            if (departmentEntry.isEmpty()) {
                throw new EntryNotFoundException("User not found.");
            }

            department = departmentEntry.get();
        }

        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoleIds() != null) {
            roles = Arrays.stream(userDTO.getRoleIds())
                    .map(id -> this.roleDAO.getRoleFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        Set<Reservation> reservations = new HashSet<>();
        if (userDTO.getReservationIds() != null) {
            reservations = Arrays.stream(userDTO.getReservationIds())
                    .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return new User(name, email, password, verified, roles, department, reservations);
    }

    /**
     * This function returns an update user, so we can save it in the database
     *
     * @param base The user data that already exist in the database
     * @param update The user data to create a new user
     * @return an updated user
     * @author Tim de Kok
     */
    public User mergeUser(User base, UserDTO update) throws EntryNotFoundException {
        base.setName(update.getName());
        base.setEmail(update.getEmail());
        base.setPassword(update.getPassword());
        base.setVerified(update.getVerified());

        Department department = null;
        if (update.getDepartmentId() != null) {
            final Optional<Department> departmentEntry = departmentDAO.getDepartmentFromDatabase(update.getDepartmentId());
            if (departmentEntry.isEmpty()) {
                throw new EntryNotFoundException("department not found.");
            }

            department = departmentEntry.get();
        }

        Set<Role> roles = new HashSet<>();
        if (update.getRoleIds() != null) {
            roles = Arrays.stream(update.getRoleIds())
                    .map(id -> this.roleDAO.getRoleFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        Set<Reservation> reservations = new HashSet<>();
        if (update.getReservationIds() != null) {
            reservations = Arrays.stream(update.getReservationIds())
                    .map(id -> this.reservationDAO.getReservationFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        base.setDepartment(null);
        base.getRoles().clear();
        base.getReservations().clear();

        base.setDepartment(department);
        base.setRoles(roles);
        base.setReservations(reservations);

        return base;
    }
}
