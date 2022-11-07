package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
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

    /**
     * This is the variable for the WingDAO in the class
     */
    private WingDAO wingDAO;

    /**
     * This is the variable for the UserDAO in the class
     */
    private UserDAO userDAO;

    /**
     * This is the constructor of the DepartmentMapper. It set the WingDAO and the UserDAO
     *
     * @param wingDAO The DAO for wing
     * @param userDAO The DAO for user
     * @author Tim de Kok
     */
    public DepartmentMapper(WingDAO wingDAO, UserDAO userDAO) {
        this.wingDAO = wingDAO;
        this.userDAO = userDAO;
    }

    /**
     * This function will be used to create a new department in the database
     *
     * @param departmentDTO The department data to create a new department
     * @return a new department
     * @author Tim de Kok
     */
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

    /**
     * This function returns an update department, so we can save it in the database
     *
     * @param base The department data that already exist in the database
     * @param update The department data to create a new department
     * @return an updated department
     * @author Tim de Kok
     */
    public Department updateDepartment(Department base, Department update) {
        base.setName(update.getName());
        base.setUsers(update.getUsers());
        base.setWings(update.getWings());

        return base;
    }
}
