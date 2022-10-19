package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.RoleDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
    private UserDAO userDAO;

    public RoleMapper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Role toRole(RoleDTO roleDTO) {
        String roleName = roleDTO.getRoleName();

        Set<User> users = new HashSet<>();
        if (roleDTO.getUserIds() != null) {
            users = Arrays.stream(roleDTO.getUserIds())
                    .map(id -> this.userDAO.getUserFromDatabase(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        Set<Permission> permissions = new HashSet<>();

        return new Role(roleName, users, permissions);
    }

    public Role mergeRole (Role base, Role update) {
        base.setName(update.getName());
        base.setUsers(update.getUsers());
        base.setPermissions(update.getPermissions());

        return base;
    }
}
