package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.PermissionDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.PermissionDTO;
import org.springframework.stereotype.Component;

import java.security.Permission;

@Component
public class PermissionMapper {
    private UserDAO userDAO;

    public PermissionMapper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

//    public Permission toPermission(PermissionDTO permissionDTO) {
//        String name = permissionDTO.getName();
}