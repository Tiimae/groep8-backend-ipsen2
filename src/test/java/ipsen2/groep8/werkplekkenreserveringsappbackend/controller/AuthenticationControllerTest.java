package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.VerifyTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

    @Mock
    private UserDAO userDAO;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private EmailService emailService;
    @Mock
    private VerifyTokenService verifyTokenService;


    private AuthenticationController authController;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        this.authController = new AuthenticationController(userRepository, jwtUtil, authenticationManager, passwordEncoder, userMapper, emailService, roleRepository, verifyTokenService);
    }

    @Test
    public void user_login_should_return_user_id_and_jwt_token() throws EntryNotFoundException, AuthenticationException, IOException {
        //Arrange
        UserDTO payload = new UserDTO();
        payload.setName("Tim");
        payload.setEmail("timblommesteijn@gmail.com");
        payload.setPassword("java1234");

        ApiResponseService response;
        User user = new User();
        user.setId("some-good-uuid");
        user.setEmail(payload.getEmail());
        user.setPassword(payload.getPassword());
        user.setVerified(true);
        //act

        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getEmail(), new ArrayList<>())).thenReturn("some-good-jwt");

        response = this.authController.login(payload, false);


        final HashMap<String, String> returnedPayload = (HashMap<String, String>) response.getPayload();
        boolean hasId = returnedPayload.containsKey("user-id");
        boolean hasToken = returnedPayload.containsKey("jwt-token");

        //Assert
        assertEquals(true, hasId);
        assertEquals(true, hasToken);

    }

//    @Test
//    public void user_register_should_return_user_id_and_jwt_token() throws EntryNotFoundException {
//        //Arrange
//        UserDTO payload = new UserDTO();
//        payload.setName("Tim");
//        payload.setEmail("timblommesteijn@gmail.com");
//        payload.setPassword("java1234");
//
//        ApiResponseService response;
//        User user = new User();
//        user.setEmail(payload.getEmail());
//        user.setName(payload.getName());
//        user.setPassword(payload.getPassword());
//        user.setVerified(false);
//
//        User newUser = user;
//        newUser.setId("some-good-uuid");
//
//        final Role role = new Role("User", new HashSet<>());
//
//        //act
//        when(this.passwordEncoder.encode(payload.getPassword())).thenReturn(payload.getPassword());
//        when(userMapper.toUser(payload)).thenReturn(user);
//        when(this.userRepository.save(user)).thenReturn(newUser);
//        when(this.roleRepository.findByName("User")).thenReturn(Optional.of(role));
//        when(jwtUtil.generateToken(user.getEmail(), new ArrayList<>())).thenReturn("some-good-token");
//        when(this.authController.profile(SecurityContextHolder.getContext().getAuthentication())).thenReturn(new ApiResponseService(HttpStatus.ACCEPTED, newUser));
//        response = this.authController.register(payload, false);
//
//        Map<String, String> returnedPayload = (Map<String, String>) response.getPayload();
//        boolean hasId = returnedPayload.containsKey("user-id");
//        boolean hasToken = returnedPayload.containsKey("jwt-token");
//
//        //Assertuser
//        assertEquals(true, hasId);
//        assertEquals(true, hasToken);

//    }

}
