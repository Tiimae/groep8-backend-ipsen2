package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthenticationController {
    private final UserRepository userRepo;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    final EmailService emailService;

    public AuthenticationController(UserRepository userRepo, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder, UserMapper userMapper, EmailService emailService) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> register(@RequestBody UserDTO user) throws EntryNotFoundException {

        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {

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

        if (!token.isBlank()) {
            try {
                this.emailService.sendMessage(newUser.getEmail(), "your account has been registered", "your account for WerkPlekkenReserveringsApp has been registered");
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
            }
        }

        return res;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> login(@RequestBody UserDTO user) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        authManager.authenticate(authInputToken);
        Map<String, Object> res = new HashMap<>();

        String token = jwtUtil.generateToken(user.getEmail());
        var foundUser = this.userRepo.findByEmail(user.getEmail());

        if(foundUser.isPresent()){
            res.put("jwt-token", token);
            res.put("user-id", foundUser.get().getId());
        }

        return res;
    }
}
