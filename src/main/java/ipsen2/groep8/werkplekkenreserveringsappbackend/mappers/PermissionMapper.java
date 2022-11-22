package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.PermissionDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {

    /**
     * This is the variable for the RoleDAO in the class
     */
    private RoleDAO roleDAO;

    /**
     * This is the constructor of the PermissionMapper. It set the RoleDAO
     *
     * @param roleDAO The DAO for role
     * @author Tim de Kok
     */
    public PermissionMapper(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    /**
     * This function will be used to create a new permission in the database
     *
     * @param permissionDTO The permission data to create a new permission
     * @return a new permission
     * @author Tim de Kok
     */
    public Permission toPermission(PermissionDTO permissionDTO) {
        String name = permissionDTO.getName();
        Set<Role> roles = new HashSet<>();

        roles = Arrays.stream(permissionDTO.getRoleIds())
                .map(id -> this.roleDAO.getRoleFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        return new Permission(name, roles);
    }

    /**
     * This function returns an update permission, so we can save it in the database
     *
     * @param base The permission data that already exist in the database
     * @param update The permission data to create a new permission
     * @return an updated permission
     * @author Tim de Kok
     */
    public Permission mergePermission(Permission base, Permission update) {
        base.setName(update.getName());
        base.setRoles(update.getRoles());

        return base;
    }
}