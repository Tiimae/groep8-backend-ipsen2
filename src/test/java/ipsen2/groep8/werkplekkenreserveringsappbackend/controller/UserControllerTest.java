package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.DepartmentRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.PasswordGeneratorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserDAO userDAO;

    private UserController userController;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private EmailService emailService;

    @Before
    public void setup() {
        this.userController = new UserController(this.userDAO, this.userMapper, this.roleRepository, this.passwordGeneratorService, this.emailService);
    }

    @Test
    public void should_return404StatusCode_when_userWithIdOneDoesNotExists() {
        //Arrange
        String userId = "1";
        ApiResponseService expectedResult = new ApiResponseService(HttpStatus.NOT_FOUND, "The user has not been found!");

        when(this.userDAO.getUserFromDatabase(userId)).thenReturn(Optional.empty());

        //Act
        ApiResponseService actualResponse = this.userController.getUser(userId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.userDAO, times(1)).getUserFromDatabase(userId);
    }
}
