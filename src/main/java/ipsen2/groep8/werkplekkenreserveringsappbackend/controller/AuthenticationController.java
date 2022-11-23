package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
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
    public ApiResponse register(@RequestBody UserDTO user) throws EntryNotFoundException {

        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {

            Map<String, Object> res = new HashMap<>();
            res.put("message", "user already exists, use the login route");

            return new ApiResponse(HttpStatus.BAD_REQUEST, res);
        }

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);

        newUser = userRepo.save(newUser);

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : newUser.getRoles()) {
            roles.add(role.getName());
        }

        String token = jwtUtil.generateToken(newUser.getEmail(), roles);

        Map<String, Object> res = new HashMap<>();

        res.put("jwt-token", token);
        res.put("user-id", newUser.getId());

        if (!token.isBlank()) {
            try {
                this.emailService.sendMessage(
                        newUser.getEmail(),
                        "CGI account registrated",
                        "<p>Hi " + newUser.getName() + ", your account for CGI has been created.</p>"
                );
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }

        return new ApiResponse(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApiResponse login(@RequestBody UserDTO user) {
        Map<String, Object> res = new HashMap<>();

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        try {
            authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED, "Wrong combination");
        }


        Optional<User> foundUser = this.userRepo.findByEmail(user.getEmail());

        if (foundUser.isPresent()) {

            final ArrayList<String> roles = new ArrayList<>();
            for (Role role : foundUser.get().getRoles()) {
                roles.add(role.getName());
            }

            String token = jwtUtil.generateToken(user.getEmail(), roles);

            res.put("jwt-token", token);
            res.put("user-id", foundUser.get().getId());
        }

        return new ApiResponse(HttpStatus.ACCEPTED, res);
    }
}
