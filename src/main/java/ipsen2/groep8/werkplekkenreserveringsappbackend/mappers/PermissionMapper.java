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
    private RoleDAO roleDAO;

    public PermissionMapper(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public Permission toPermission(PermissionDTO permissionDTO) {
        String name = permissionDTO.getName();
        Set<Role> roles = new HashSet<>();

        roles = Arrays.stream(permissionDTO.getRoleIds())
                .map(id -> this.roleDAO.getRoleFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        return new Permission(name, roles);
    }

    public Permission mergePermission(Permission base, Permission update) {
        base.setName(update.getName());
        base.setRoles(update.getRoles());

        return base;
    }
}