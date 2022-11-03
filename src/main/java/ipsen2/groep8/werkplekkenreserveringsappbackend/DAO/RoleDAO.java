package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import jdk.jfr.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class RoleDAO {

    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    public RoleDAO(RoleRepository roleRepository, @Lazy RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public Optional<Role> getRoleFromDatabase(String roleid) {
        return this.roleRepository.findById(roleid);
    }

    public List<Role> getAllRolesFromDatabase() {
        return this.roleRepository.findAll();
    }

    public void saveRoleToDatabase(Role role) {
        this.roleRepository.save(role);
    }

    public void updateRoleToDatabase(String id, Role roleUpdate) {
        Role role = this.roleRepository.getById(id);
        role = this.roleMapper.mergeRole(role, roleUpdate);
        this.roleRepository.saveAndFlush(role);
    }

    public void removeRoleToDatabase(String roleId) {
        final Role role = this.roleRepository.findById(roleId).get();

        for (User user : role.getUsers()) {
            role.getUsers().remove(user);
        }

        for (Permission permission : role.getPermissions()) {
            permission.getRoles().remove(role);
            role.getPermissions().remove(permission);
        }

        this.roleRepository.deleteById(roleId);
    }

}
