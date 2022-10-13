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
public class UserDAO {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public UserDAO(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> getUserFromDatabase(String userid) {
        return this.userRepository.findById(userid);
    }

    public List<User> getAllUsersFromDatabase() {
        return this.userRepository.findAll();
    }

    public void saveUserToDatabase(User user) {
        this.userRepository.save(user);
    }

    public void updateUserInDatabase(User user) {
        this.userRepository.save(user);
    }

    public void deleteUserFromDatabase(String userid) {
        final User user = this.userRepository.findById(userid).get();
        for (Role role : user.getRoles()) {
            user.getRoles().remove(role);
        }

        this.userRepository.deleteById(userid);
    }

    public void appendUserToRole(String roleid, String userid) {
        final Role role = this.roleRepository.findById(roleid).get();
        final User user = this.userRepository.findById(userid).get();

        final Set<Role> roles = user.getRoles();
        roles.add(role);

        user.setRoles(roles);
        this.userRepository.save(user);
    }
}
