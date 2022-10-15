package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/user")
public class UserController {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Optional<User>> getUser(@PathVariable String userid) {
        Optional<User> user = this.userDAO.getUserFromDatabase(userid);
        if (user.isEmpty()) {
             return new ApiResponse(HttpStatus.NOT_FOUND, "The user has not been found!");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, user);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<User>> getUsers() {
        List<User> allUsers = this.userDAO.getAllUsersFromDatabase();

        return new ApiResponse(HttpStatus.ACCEPTED, allUsers);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postUser(@RequestBody User user) {
        final String hash = this.authenticationService.hash(user.getPassword().toCharArray());
        user.setPassword(hash);
        this.userDAO.saveUserToDatabase(user);

        return new ApiResponse(HttpStatus.CREATED, "User has been posted to the database!");
    }

    @PutMapping(value = "")
    @ResponseBody
    public ApiResponse updateUser(@RequestBody User user) {
        this.userDAO.updateUserInDatabase(user);

        return new ApiResponse(HttpStatus.ACCEPTED, "User has been updated");
    }

    @DeleteMapping(value = "/{userid}")
    @ResponseBody
    public ApiResponse deleteUser(@PathVariable String userid) {
        this.userDAO.deleteUserFromDatabase(userid);
        return new ApiResponse(HttpStatus.ACCEPTED, "User has been deleted");
    }

    @RequestMapping(value = "/{userid}/role/{roleid}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse appendRoleToUser(@PathVariable String roleid, @PathVariable String userid) {
        this.userDAO.appendUserToRole(roleid, userid);
        return new ApiResponse(HttpStatus.ACCEPTED, "Role has been added to user!");
    }
}
