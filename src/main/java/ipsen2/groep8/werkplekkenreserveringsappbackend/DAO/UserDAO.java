package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {
    private UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        this.userRepository.deleteById(userid);
    }
}
