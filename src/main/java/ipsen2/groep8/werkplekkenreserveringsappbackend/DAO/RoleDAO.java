package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Component
public class RoleDAO {

    /**
     * This is the variable for the RoleRepository in the class
     */
    private RoleRepository roleRepository;

    /**
     * This is the variable for the RoleMapper in the class
     */
    private RoleMapper roleMapper;

    /**
     * This is the constructor of the RoleDAO. It set the roleMapper and roleRepository
     *
     * @param roleMapper The Mapper for role
     * @param roleRepository The repository for role
     * @author Tim de Kok
     */
    public RoleDAO(RoleRepository roleRepository, @Lazy RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Get a specific role out of the database by id and returns that role
     *
     * @param roleid The id of the role you want to have
     * @return A role out of the database
     * @author Tim de Kok
     */
    public Optional<Role> getRoleFromDatabase(String roleid) {
        return this.roleRepository.findById(roleid);
    }

    /**
     * Get all roles out of the database and returns it as a list of roles
     *
     * @return A list of roles out of the database
     * @author Tim de Kok
     */
    public List<Role> getAllRolesFromDatabase() {
        return this.roleRepository.findAll();
    }

    /**
     * Save a new role to the database
     *
     * @param role The role what needs to be safed in the database
     * @author Tim de Kok
     */
    public void saveRoleToDatabase(Role role) {
        this.roleRepository.save(role);
    }

    /**
     * Update a existing role in the database
     *
     * @param id The id of the role what needs to be update
     * @param roleUpdate The updated version of the role
     * @author Tim de Kok
     */
    public void updateRoleToDatabase(String id, Role roleUpdate) {
        Role role = this.roleRepository.getById(id);
        role = this.roleMapper.mergeRole(role, roleUpdate);
        this.roleRepository.saveAndFlush(role);
    }

    /**
     * Removes a role out of the database
     *
     * @param roleId Id of the role what needs to be removed
     * @author Tim de Kok
     */
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
