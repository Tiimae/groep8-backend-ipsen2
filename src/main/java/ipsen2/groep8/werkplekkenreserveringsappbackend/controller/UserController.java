package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;

@Controller
@RequestMapping(value = "/api/user")
public class UserController {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    public UserController(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<User> getUser(@PathVariable String userid) {
        Optional<User> user = this.userDAO.getUserFromDatabase(userid);

        if (user.isEmpty()) {
             return new ApiResponse(HttpStatus.NOT_FOUND, "The user has not been found!");
        }

        User safeUser = user.get();
        safeUser.setPassword("");

        return new ApiResponse(HttpStatus.ACCEPTED, safeUser);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<User>> getUsers() {
        return new ApiResponse(HttpStatus.ACCEPTED, this.userDAO.getAllUsersFromDatabase());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponse<User> postUser(@RequestBody @Valid UserDTO userDTO) throws EntryNotFoundException {
        User user = userMapper.toUser(userDTO);
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        this.userDAO.saveUserToDatabase(user);
        return new ApiResponse(HttpStatus.CREATED, user);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse updateUser(@PathVariable String id, @RequestBody @Valid UserDTO userDTO) throws EntryNotFoundException {
        User user = this.userMapper.toUser(userDTO);
        this.userDAO.updateUserInDatabase(id, user);

        return new ApiResponse(HttpStatus.ACCEPTED, "User has been updated");
    }

    @DeleteMapping(value = "/{userid}")
    @ResponseBody
    public ApiResponse deleteUser(@PathVariable String userid) {
        this.userDAO.deleteUserFromDatabase(userid);
        return new ApiResponse(HttpStatus.ACCEPTED, "User has been deleted");
    }

    @GetMapping(value = "/{userid}/reservations")
    @ResponseBody
    public ApiResponse<List<Reservation>> getUserReservations(@PathVariable String userid, @RequestParam(required = false) String filter) throws EntryNotFoundException {
        Optional<User> userEntry = this.userDAO.getUserFromDatabase(userid);
        if (userEntry.isEmpty()) throw new EntryNotFoundException("The user has not been found!");
        User presentUser = userEntry.get();
        Set<Reservation> filteredReservations = new HashSet<>();

        if(filter != null && filter.equals("week")){
            for (Reservation reservation : presentUser.getReservations()) {
                if(LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == reservation.getStartDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())){
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponse(HttpStatus.OK, filteredReservations);
        }

        if(filter != null && filter.equals("month")){
            for (Reservation reservation : presentUser.getReservations()) {
                if(LocalDate.now().getMonth() == reservation.getStartDate().getMonth()){
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponse(HttpStatus.OK, filteredReservations);
        }

        return new ApiResponse(HttpStatus.OK, presentUser.getReservations());
    }

}
