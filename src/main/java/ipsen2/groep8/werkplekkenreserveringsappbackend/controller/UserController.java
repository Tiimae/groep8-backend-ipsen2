package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.PasswordGeneratorService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.thread.MailThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @author Tim de Kok, Frederik Coster
 * @version 1.0
 */
@RestController
@RequestMapping(

)
public class UserController {

    /**
     * This is the variable for the UserDAO in the class
     */
    private final UserDAO userDAO;

    /**
     * This is the variable for the UserMapper in the class
     */
    private final UserMapper userMapper;

    /**
     * This is the variable for the PasswordEncoder in the class
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    private RoleRepository roleRepository;

    private PasswordGeneratorService passwordGeneratorService;

    private EmailService emailService;

    /**
     * This is the constructor of the UserController. It set the UserDAO and the UserMapper
     *
     * @param userDAO    The DAO for user
     * @param userMapper The mapper for user
     * @author Tim de Kok
     */
    public UserController(UserDAO userDAO, UserMapper userMapper, RoleRepository roleRepository, PasswordGeneratorService passwordGeneratorService, EmailService emailService) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordGeneratorService = passwordGeneratorService;
        this.emailService = emailService;
    }

    /**
     * This function returns an ApiResponse with a status code and a specific user what will be returned from the userDAO
     *
     * @param userId The user id what we get from the url
     * @return an ApiResponse with a statuscode and a user
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getUser)
    @ResponseBody
    public ApiResponseService<User> getUser(@PathVariable String userId) {
        Optional<User> user = this.userDAO.getUserFromDatabase(userId);

        if (user.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The user has not been found!");
        }

        User safeUser = user.get();

        return new ApiResponseService(HttpStatus.FOUND, safeUser);
    }

    /**
     * This function get all the users in the database and returns all the users as a List
     *
     * @return an ApiResponse with a statuscode and a list of all users
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getAllUsers)
    @ResponseBody
    public ApiResponseService<List<User>> getUsers() {
        final List<User> allUsersFromDatabase = this.userDAO.getAllUsersFromDatabase();

        for (User user : allUsersFromDatabase) {
            user.setPassword("");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, allUsersFromDatabase);
    }

    /**
     * This function creates an new user in the database and return the specific user back
     *
     * @param userDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the user what just got created
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     * @author Tim de Kok
     */
    @PostMapping(value = ApiConstant.getAllUsers, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<User> postUser(@RequestBody @Valid UserDTO userDTO) throws EntryNotFoundException {
        User user = userMapper.toUser(userDTO);
        user.setPassword(this.passwordGeneratorService.generate(10));

        MailThread mail = new MailThread(
                user.getEmail(),
                "CGI account password",
                "<p>Hi " + user.getName() + ", here is your password what you van use once:" + user.getPassword() + "</p>",
                this.emailService
        );
        mail.start();

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        final Role role = this.roleRepository.findByName("User").get();
        boolean hasRole = false;

        for (Role currentRole : user.getRoles()) {
            if (currentRole.getName().equals(role.getName())) {
                hasRole = true;
                break;
            }
        }

        if (!hasRole) {
            user.addRoles(role);
        }

        this.userDAO.saveUserToDatabase(user);
        user.setPassword("");
        return new ApiResponseService(HttpStatus.CREATED, user);
    }

    /**
     * This function updates an user and returns the user what just got updated back
     *
     * @param userId  This is the user id that passed into the url
     * @param userDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the user what just got updated
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     * @author Tim de Kok
     */
    @PutMapping(value = ApiConstant.getUser, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateUser(@PathVariable String userId, @RequestBody @Valid UserDTO userDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userDAO.updateUserInDatabase(userId, userDTO));
    }

    /**
     * This function removes an user from the database and send an Api response back
     *
     * @param userId The user id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = ApiConstant.getUser)
    @ResponseBody
    public ApiResponseService deleteUser(@PathVariable String userId) {
        this.userDAO.deleteUserFromDatabase(userId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "User has been deleted");
    }

    /**
     * This function gets all the reservations from a user in the database and returns all the (filtered)reservations as a List
     *
     * @param userId The user id from where you want to grab the reservations from
     * @param filter A filter(week or month) passed as parameters in url to filter the reservation results
     * @return an ApiResponse with a statuscode and a list of (filtered)reservations belonging to the user
     * @author Frederik Coster
     */
    @GetMapping(value = ApiConstant.getUserReservations)
    @ResponseBody
    public ApiResponseService<List<Reservation>> getUserReservations(@PathVariable String userId, @RequestParam(required = false) String filter) throws EntryNotFoundException {
        Optional<User> userEntry = this.userDAO.getUserFromDatabase(userId);
        if (userEntry.isEmpty()) throw new EntryNotFoundException("The user has not been found!");
        User presentUser = userEntry.get();
        Set<Reservation> filteredReservations = new HashSet<>();

        if (filter != null && filter.equals("week")) {
            for (Reservation reservation : presentUser.getReservations()) {
                if (LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == reservation.getStartDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())) {
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        if (filter != null && filter.equals("month")) {
            for (Reservation reservation : presentUser.getReservations()) {
                if (LocalDate.now().getMonth() == reservation.getStartDate().getMonth()) {
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        return new ApiResponseService(HttpStatus.OK, presentUser.getReservations());
    }

}
