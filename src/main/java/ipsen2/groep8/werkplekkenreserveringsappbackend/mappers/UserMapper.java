package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private DepartmentDAO departmentDAO;
    private RoleDAO roleDAO;
    private ReservationDAO reservationDAO;

    public UserMapper(DepartmentDAO departmentDAO, RoleDAO roleDAO, ReservationDAO reservationDAO) {
        this.departmentDAO = departmentDAO;
        this.roleDAO = roleDAO;
        this.reservationDAO = reservationDAO;
    }

    public User toUser(UserDTO userDTO) throws EntryNotFoundException {
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

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

        return new User(name, email, password, roles, department, reservations);
    }

    public User mergeUser(User base, User update) {
        base.setName(update.getName());
        base.setEmail(update.getEmail());
        base.setPassword(update.getPassword());
        base.setDepartment(update.getDepartment());
        base.setRoles(update.getRoles());
        base.setReservations(update.getReservations());

        return base;
    }
}
