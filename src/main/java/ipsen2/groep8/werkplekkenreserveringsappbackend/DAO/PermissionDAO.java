package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.PermissionRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.PermissionMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PermissionDAO {

    private PermissionRepository permissionRepository;

    public PermissionDAO(PermissionRepository permissionRepository, @Lazy PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
    }

    public Optional<Permission> getPermissionFromDatabase(String permissionid) {
        return this.permissionRepository.findById(permissionid);
    }

    public List<Permission> getAllPermissionsFromDatabase() {
        return this.permissionRepository.findAll();
    }

    public void savePermissionToDatabase(Permission permission) {
        this.permissionRepository.save(permission);
    }

    public void updatePermissionInDatabase(Permission permissionUpdate) {
        this.permissionRepository.saveAndFlush(permissionUpdate);
    }

    public void deletePermissionFromDatabase(String id) {
        final Permission permission = this.permissionRepository.getById(id);

        for (Role role : permission.getRoles()) {
            role.getPermissions().remove(permission);
            permission.getRoles().remove(role);
        }

        this.permissionRepository.deleteById(id);
    }
}