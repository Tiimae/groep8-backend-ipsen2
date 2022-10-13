package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value = "/register", consumes = {"application/json"})
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail());

        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping(value = "/login", consumes = {"application/json"})
    @ResponseBody
    public Map<String, Object> login(@RequestBody User user) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        authManager.authenticate(authInputToken);

        String token = jwtUtil.generateToken(user.getEmail());

        return Collections.singletonMap("jwt-token", token);
    }
}
