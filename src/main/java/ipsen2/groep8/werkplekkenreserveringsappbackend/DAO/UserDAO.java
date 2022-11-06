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

    /**
     * This is the variable for the UserRepository in the class
     */
    private UserRepository userRepository;

    /**
     * This is the variable for the UserMapper in the class
     */
    private UserMapper userMapper;

    /**
     * This is the constructor of the UserDAO. It set the userMapper and userRepository
     *
     * @param userMapper The Mapper for user
     * @param userRepository The repository for user
     * @author Tim de Kok
     */
    public UserDAO(UserRepository userRepository, @Lazy UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Get a specific user out of the database by id and returns that user
     *
     * @param userid The id of the user you want to have
     * @return A user out of the database
     * @author Tim de Kok
     */
    public Optional<User> getUserFromDatabase(String userid) {
        return this.userRepository.findById(userid);
    }

    /**
     * Get all users out of the database and returns it as a list of users
     *
     * @return A list of users out of the database
     * @author Tim de Kok
     */
    public List<User> getAllUsersFromDatabase() {
        return this.userRepository.findAll();
    }

    /**
     * Save a new user to the database
     *
     * @param user The user what needs to be safed in the database
     * @author Tim de Kok
     */
    public void saveUserToDatabase(User user) {
        this.userRepository.save(user);
    }

    /**
     * Update a existing user in the database
     *
     * @param id The id of the user what needs to be update
     * @param userUpdate The updated version of the user
     * @author Tim de Kok
     */
    public void updateUserInDatabase(String id, User userUpdate) {
        User user = this.userRepository.getById(id);
        this.userMapper.mergeUser(user, userUpdate);
        this.userRepository.saveAndFlush(user);
    }

    /**
     * Removes a user out of the database
     *
     * @param userid Id of the user what needs to be removed
     * @author Tim de Kok
     */
    public void deleteUserFromDatabase(String userid) {
        final User user = this.userRepository.findById(userid).get();
        for (Role role : user.getRoles()) {
            user.getRoles().remove(role);
        }

        if(user.getDepartment() != null){
            user.getDepartment().getUsers().remove(user);
            user.setDepartment(null);
        }


        this.userRepository.deleteById(userid);
    }
}
