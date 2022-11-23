package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.security.JWTUtil;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

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


    private AuthenticationController authController;

    @Mock
    private UserMapper userMapper;

    @Before
    public void setup() {
        this.authController = new AuthenticationController(userRepository, jwtUtil, authenticationManager, passwordEncoder, userMapper, emailService);
    }

    @Test
    public void user_login_should_return_user_id_and_jwt_token() throws EntryNotFoundException {
        //Arrange
//        UserDTO payload = new UserDTO();
//        payload.setName("Tim");
//        payload.setEmail("timblommesteijn@gmail.com");
//        payload.setPassword("java1234");
//
//        ApiResponseService response;
//        User user = new User();
//        user.setId("some-good-uuid");
//        user.setEmail(payload.getEmail());
//        user.setPassword(payload.getPassword());
        //act

//        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(jwtUtil.generateToken(user.getEmail())).thenReturn("some-good-jwt");
//
//        try{
//            response = this.authController.login(payload);
//        }catch (AuthenticationException e) {
//            e.printStackTrace();
//        }

//        boolean hasId = response.getPayload().get("user-id");
//        boolean hasToken = response.containsKey("jwt-token");
//
//        //Assert
//        assertEquals(true, hasId);
//        assertEquals(true, hasToken);

    }

    @Test
    public void user_register_should_return_user_id_and_jwt_token() throws EntryNotFoundException {
        //Arrange
        UserDTO payload = new UserDTO();
        payload.setName("Tim");
        payload.setEmail("timblommesteijn@gmail.com");
        payload.setPassword("java1234");

        Map response = new HashMap();
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setName(payload.getName());
        user.setPassword(payload.getPassword());

        User newUser = user;
        newUser.setId("some-good-uuid");

        //act
        when(this.passwordEncoder.encode(payload.getPassword())).thenReturn(payload.getPassword());
        when(userMapper.toUser(payload)).thenReturn(user);
        when(this.userRepository.save(user)).thenReturn(newUser);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("some-good-token");
//        response = this.authController.register(payload);


//        boolean hasId = response.containsKey("user-id");
//        boolean hasToken = response.containsKey("jwt-token");
//
//        //Assertuser
//        assertEquals(true, hasId);
//        assertEquals(true, hasToken);

    }

}
