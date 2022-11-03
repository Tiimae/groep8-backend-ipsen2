package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {


    @Autowired private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    public AuthenticationController(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> register(@RequestBody UserDTO user, HttpServletResponse response) throws EntryNotFoundException {

        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()){

            Map<String, Object> res = new HashMap<>();
            res.put("message", "user already exists, use the login route");

            return res;
        }

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);

        newUser = userRepo.save(newUser);
        String token = jwtUtil.generateToken(newUser.getEmail());

        Map<String, Object> res = new HashMap<>();
        res.put("jwt-token", token);
        res.put("user-id", newUser.getId());

        return res;
    }

    @PostMapping(value = "/login",  consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> login(@RequestBody UserDTO user) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authInputToken =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        authManager.authenticate(authInputToken);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> res = new HashMap<>();
        res.put("jwt-token", token);
        res.put("user-id", this.userRepo.findByEmail(user.getEmail()).get().getId());

        return res;
    }
}
