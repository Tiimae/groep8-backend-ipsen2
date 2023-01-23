package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.VerifyTokenRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.VerifyToken;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private VerifyTokenRepository verifyTokenRepository;
    private ReservationDAO reservationDAO;

    /**
     * This is the constructor of the UserDAO. It set the userMapper and userRepository
     *
     * @param userMapper The Mapper for user
     * @param userRepository The repository for user
     * @author Tim de Kok
     */
    public UserDAO(UserRepository userRepository, @Lazy UserMapper userMapper, VerifyTokenRepository verifyTokenRepository, ReservationDAO reservationDAO) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.verifyTokenRepository = verifyTokenRepository;
        this.reservationDAO = reservationDAO;
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
    public Optional<User> getUserFromDatabaseByEmail(String email) {
        return this.userRepository.findByEmail(email);
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
     * @param userDTO The updated version of the user
     * @author Tim de Kok
     */
    public User updateUserInDatabase(String id, UserDTO userDTO) throws EntryNotFoundException {

        final Optional<User> byId = this.userRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        final User user = this.userMapper.mergeUser(byId.get(), userDTO);

        return this.userRepository.saveAndFlush(user);
    }

    /**
     * Removes a user out of the database
     *
     * @param userid Id of the user what needs to be removed
     * @author Tim de Kok
     */
    public void deleteUserFromDatabase(String userid) {
        final Optional<User> user = this.userRepository.findById(userid);

        if (user.isEmpty()) {
            return;
        }

        User finalUser = user.get();

        final List<VerifyToken> verifyTokenByUser = this.verifyTokenRepository.getVerifyTokenByUser(finalUser);

        if (!verifyTokenByUser.isEmpty()){
            for (VerifyToken token : verifyTokenByUser) {
                token.setUser(null);
                this.verifyTokenRepository.delete(token);
            }
        }

        if (!finalUser.getRoles().isEmpty()) {
            finalUser.getRoles().clear();
        }

        if(finalUser.getDepartment() != null){
            finalUser.getDepartment().getUsers().remove(finalUser);
            finalUser.setDepartment(null);
        }

        if (!finalUser.getReservations().isEmpty()) {
            for(Reservation reservation : finalUser.getReservations()) {
                this.reservationDAO.deleteReservationFromDatabase(reservation.getId());
            }
        }


        this.userRepository.delete(finalUser);
    }
}
