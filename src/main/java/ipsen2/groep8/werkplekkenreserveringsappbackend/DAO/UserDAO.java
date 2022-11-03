package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserDAO {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserDAO(UserRepository userRepository, @Lazy UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    public void updateUserInDatabase(String id, User userUpdate) {
        User user = this.userRepository.getById(id);
        this.userMapper.mergeUser(user, userUpdate);
        this.userRepository.saveAndFlush(user);
    }

    public void deleteUserFromDatabase(String userid) {
        final User user = this.userRepository.findById(userid).get();
        for (Role role : user.getRoles()) {
            user.getRoles().remove(role);
        }

        user.getDepartment().getUsers().remove(user);
        user.setDepartment(null);

        this.userRepository.deleteById(userid);
    }
}
