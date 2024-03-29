package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

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
        Set<User> users = this.getAllUsers(departmentDTO.getUserIds());
        Set<Wing> wings = this.getAllwings(departmentDTO.getWingIds());

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
    public Department updateDepartment(Department base, DepartmentDTO update) {
        base.setName(update.getName());

        for (User user : this.getAllUsers(update.getUserIds())) {
            if (!base.getUsers().contains(user)) {
                base.addUser(user);
            }
        }

        for (Wing wing : this.getAllwings(update.getWingIds())) {
            if (!base.getWings().contains(wing)) {
                base.getWings().add(wing);
            }
        }

        return base;
    }

    public Set<User> getAllUsers(String[] userIds) {
        Set<User> users = new HashSet<>();
        if (userIds != null) {
            users = Arrays.stream(userIds)
                    .map(id -> this.userDAO.getUserFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return users;
    }

    public Set<Wing> getAllwings(String[] wingIds) {
        Set<Wing> wings = new HashSet<>();
        if (wingIds != null) {
            wings = Arrays.stream(wingIds)
                    .map(id -> this.wingDAO.getWingFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return wings;
    }
}
