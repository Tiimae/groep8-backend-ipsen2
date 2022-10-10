package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/user")
public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public Optional<User> getUser(@PathVariable String userid) {
        return this.userDAO.getUserFromDatabase(userid);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers() {
        return this.userDAO.getAllUsersFromDatabase();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public String postUser(@RequestBody User user) {
        this.userDAO.saveUserToDatabase(user);
        return "User has been posted to the database";
    }

    @PutMapping(value = "")
    @ResponseBody
    public String updateUser(@RequestBody User user) {
        this.userDAO.updateUserInDatabase(user);
        return "User has been updated";
    }

    @DeleteMapping(value = "/{userid}")
    @ResponseBody
    public String deleteUser(@PathVariable String userid) {
        this.userDAO.deleteUserFromDatabase(userid);
        return "User has been deleted";
    }
}
