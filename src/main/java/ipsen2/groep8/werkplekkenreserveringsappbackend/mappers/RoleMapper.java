package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.RoleDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    /**
     * This is the variable for the UserDAO in the class
     */
    private UserDAO userDAO;

    /**
     * This is the constructor of the RoleMapper. It set the UserDAO
     *
     * @param userDAO The DAO for user
     * @author Tim de Kok
     */
    public RoleMapper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * This function will be used to create a new role in the database
     *
     * @param roleDTO The role data to create a new role
     * @return a new role
     * @author Tim de Kok
     */
    public Role toRole(RoleDTO roleDTO) {
        String roleName = roleDTO.getRoleName();

        Set<User> users = this.getAllUsers(roleDTO.getUserIds());

        return new Role(roleName, users);
    }

    /**
     * This function returns an update role, so we can save it in the database
     *
     * @param base The role data that already exist in the database
     * @param update The role data to create a new role
     * @return an updated role
     * @author Tim de Kok
     */
    public Role mergeRole (Role base, RoleDTO update) {
        base.setName(update.getRoleName());

        for (User user : this.getAllUsers(update.getUserIds())) {
            if (!base.getUsers().contains(user)) {
                base.addUser(user);
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
}
