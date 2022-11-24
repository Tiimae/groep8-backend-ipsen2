package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping(
        headers = "Accept=application/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthenticationController {
    private final UserRepository userRepo;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    final EmailService emailService;
    private RoleRepository roleRepository;

    @Value("${jwt_secret}")
    private String jwtSecret;
    public AuthenticationController(UserRepository userRepo, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder, UserMapper userMapper, EmailService emailService, RoleRepository roleRepository) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = ApiConstant.toCookie, consumes = MediaType.ALL_VALUE)
    public ModelAndView redirectWithUsingForwardPrefix(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("attribute", "forwardWithForwardPrefix");
        response.addCookie(createCookie());
        return new ModelAndView("redirect:" + request.getHeader(HttpHeaders.REFERER) + "login", model);
    }

    @GetMapping(value = ApiConstant.secret, consumes = MediaType.ALL_VALUE)
    @ResponseBody()
//    @CrossOrigin(maxAge = 360/0, )
    public ApiResponseService secret(HttpServletRequest request){
        String secret = null;

        if(request.getCookies() != null){
            secret =  Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("secret"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if(secret == null || secret.isBlank() || secret.isEmpty()){
            return new ApiResponseService(HttpStatus.FORBIDDEN, "You are not authenticated");
        }

        secret = new EncryptionService().decrypt(secret, jwtSecret);

        return new ApiResponseService(HttpStatus.ACCEPTED, secret);
    }

    @PostMapping(value = ApiConstant.register)
    @ResponseBody
    public ApiResponseService register(@RequestBody UserDTO user) throws EntryNotFoundException {

        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {

            Map<String, Object> res = new HashMap<>();
            res.put("message", "user already exists, use the login route");

            return new ApiResponseService(HttpStatus.BAD_REQUEST, res);
        }


        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);
        newUser.addRoles(this.roleRepository.findByName("User").get());

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
        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = ApiConstant.login)
    @ResponseBody
    public ApiResponseService login(@RequestBody UserDTO user) throws AuthenticationException, IOException {
        final HashMap<String, String> res = new HashMap<>();
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        try {
            authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            return new ApiResponseService(HttpStatus.UNAUTHORIZED, "Wrong combination");
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
            res.put("destination", "/to-cookie");
        }


        return new ApiResponseService(HttpStatus.ACCEPTED, res);
    }

    public String createSecret(){
        String randomSecret = Date.from(Instant.now()).toString() + String.valueOf((new Random()).nextInt());
        return EncryptionService.getMd5(randomSecret);
    }

    private Cookie createCookie(){
        String secret = this.createSecret();
        secret = new EncryptionService().encrypt(secret, jwtSecret);
        Cookie cookie = new Cookie("secret", secret);

        cookie.setHttpOnly(true);
        cookie.setPath(ApiConstant.secret);
        //expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.set
        cookie.setDomain("localhost");
        return cookie;
    }
}
