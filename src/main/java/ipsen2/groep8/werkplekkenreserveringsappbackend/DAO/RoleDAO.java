package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class RoleDAO {

    private RoleRepository roleRepository;

    public RoleDAO(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleFromDatabase(String roleid) {
        return this.roleRepository.getById(roleid);
    }

    public List<Role> getAllRolesFromDatabase() {
        return this.roleRepository.findAll();
    }

    public void saveRoleToDatabase(Role role) {
        this.roleRepository.save(role);
    }

    public void updateRoleToDatabase(Role role) {
        this.roleRepository.save(role);
    }

    public void removeRoleToDatabase(String roleId) {
        final Role role = this.roleRepository.findById(roleId).get();

        for (User user : role.getUsers()) {
            role.getUsers().remove(user);
        }

        this.roleRepository.deleteById(roleId);
    }

}
