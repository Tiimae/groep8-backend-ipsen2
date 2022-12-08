package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EncryptionService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.VerifyTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
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
    private final EmailService emailService;
    private RoleRepository roleRepository;

    private final VerifyTokenService verifyTokenService;

    @Value("${shared_secret}")
    private String sharedSecret;
    @Value("${jwt_secret}")
    private String jwtSecret;
    public AuthenticationController(UserRepository userRepo, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder, UserMapper userMapper, EmailService emailService, RoleRepository roleRepository, VerifyTokenService verifyTokenService) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.verifyTokenService = verifyTokenService;
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
    public ApiResponseService register(@Valid @RequestBody UserDTO user, @RequestParam boolean encrypted ) throws EntryNotFoundException {

        Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {

            Map<String, Object> res = new HashMap<>();
            res.put("message", "User with this email already exists, change your email to continue");

            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        String encodedPass = passwordEncoder.encode(
                encrypted
                        ? EncryptionService.decryptAes(user.getPassword(), sharedSecret)
                        : user.getPassword()
        );
        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);
        newUser.addRoles(this.roleRepository.findByName("User").get());

        newUser = userRepo.save(newUser);

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : newUser.getRoles()) {
            roles.add(role.getName());
        }

        // Send email with new verify-token
        this.sendVerifyToken(newUser.getId());

        // response
        Map<String, Object> res = new HashMap<>();
        res.put("message", "Successfully created your account");

        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @GetMapping(value = ApiConstant.sendVerifyToken, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> sendVerifyToken(@PathVariable String userId) {

        Map<String, Object> res = new HashMap<>();

        Optional<User> foundUser = userRepo.findById(userId);
        if (foundUser.isPresent()) {
            User user = foundUser.orElse(null);

            if(foundUser.get().getVerified()){
                res.put("message", "This user is already verified, cant send a verify token");
                return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
            }

            // Create and save Token in DB
            String token = UUID.randomUUID().toString();
            VerifyToken verifyToken = new VerifyToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            verifyTokenService.saveVerifyToken(verifyToken);

            // Send verification mail
//            try {
//                this.emailService.sendMessage(
//                        foundUser.get().getEmail(),
//                        "CGI account verify email",
//                        "<p>Hi " + foundUser.get().getName() + ", here is your code to verify your email:"+token+"</p>"
//                );
//            } catch (Throwable e) {
//                System.out.println(e.getMessage());
//            }

            // Response
            res.put("message", "Successfully sent a verify token to " + foundUser.get().getEmail());
            return new ApiResponseService<>(HttpStatus.ACCEPTED, res);

        }

        res.put("message", "The user you are trying to verify was not found");
        return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);

    }

    @GetMapping(value = ApiConstant.confirmVerifyToken, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> confirmVerifyToken(@PathVariable String token) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> bearerUser = this.profile(SecurityContextHolder.getContext().getAuthentication()).getPayload();
        Optional<VerifyToken> verifyToken = this.verifyTokenService.getToken(token);


        if(!bearerUser.isPresent()){
            res.put("message", "Something went wrong, please try again in a moment");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        if(!verifyToken.isPresent()){
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        String bearerUserId = bearerUser.get().getId();
        String tokenUserId = verifyToken.get().getUser().getId();

        if(bearerUserId.equals(tokenUserId)){
            try {
                verifyTokenService.confirmToken(token);
                res.put("message", "Successfully confirmed your email. Redirecting you...");
                return new ApiResponseService<>(HttpStatus.OK, res);
            } catch (Exception e) {
                res.put("message", e.getMessage());
                return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
            }
        }

        else {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }


    }

    @PostMapping(value = ApiConstant.login)
    @ResponseBody
    public ApiResponseService login(@RequestBody UserDTO user, @RequestParam boolean encrypted) throws AuthenticationException, IOException {
        final HashMap<String, String> res = new HashMap<>();

        if(encrypted){
            user.setPassword(EncryptionService.decryptAes(user.getPassword(), sharedSecret));
        }

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        try {
            authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            return new ApiResponseService<HashMap<String, String>>(HttpStatus.UNAUTHORIZED, "Wrong combination");
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
            res.put("verified", foundUser.get().getVerified().toString());
            res.put("destination", "/to-cookie");
        }


        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @GetMapping(value = ApiConstant.profile, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Optional<User>> profile(Principal securityPrincipal) {

        Optional<User> foundUser =  this.userRepo.findByEmail(securityPrincipal.getName());

        return new ApiResponseService<>(
                HttpStatus.ACCEPTED,
                foundUser
        );
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
