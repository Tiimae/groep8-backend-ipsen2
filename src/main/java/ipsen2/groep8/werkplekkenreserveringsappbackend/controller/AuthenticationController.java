package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDAO userDAO;

    public AuthenticationController(UserDAO userDAO) {
        this.authenticationService = new AuthenticationService(AuthenticationService.DEFAULT_COST);
        this.userDAO = userDAO;
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    @ResponseBody
    public String register(@RequestBody User user) {
        this.userDAO.saveUserToDatabase(user);
        return "a";
    }

    @PostMapping(value = "/login", consumes = {"application/json"})
    @ResponseBody
    public String login(@RequestBody User user) {
//        this.buildingDAO.updateBuildingInDatabase(building);
        return "User has been updated";
    }
}
