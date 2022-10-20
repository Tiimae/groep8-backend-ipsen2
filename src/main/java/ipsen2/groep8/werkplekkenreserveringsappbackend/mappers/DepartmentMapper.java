package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {
    private WingDAO wingDAO;
    private UserDAO userDAO;

    public DepartmentMapper(WingDAO wingDAO, UserDAO userDAO) {
        this.wingDAO = wingDAO;
        this.userDAO = userDAO;
    }

    public Department toDepartment(DepartmentDTO departmentDTO) {
        String name = departmentDTO.getName();

        Set<User> users = new HashSet<>();
        users = Arrays.stream(departmentDTO.getUserIds())
                .map(id -> this.userDAO.getUserFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        Set<Wing> wings = new HashSet<>();
        wings = Arrays.stream(departmentDTO.getWingIds())
                .map(id -> this.wingDAO.getWingFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        return new Department(name, users, wings);
    }

    public Department updateDepartment(Department base, Department update) {
        base.setName(update.getName());
        base.setUsers(update.getUsers());
        base.setWings(update.getWings());

        return base;
    }
}
