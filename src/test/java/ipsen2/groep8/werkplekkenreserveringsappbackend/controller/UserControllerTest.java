package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserDAO userDAO;

    private UserController userController;

    @Mock
    private UserMapper userMapper;

    @Before
    public void setup() {
        this.userController = new UserController(this.userDAO, this.userMapper);
    }

    @Test
    public void should_return404StatusCode_when_userWithIdOneDoesNotExists() {
        //Arrange
        String userId = "1";
        ApiResponse expectedResult = new ApiResponse(HttpStatus.NOT_FOUND, "The user has not been found!");

        when(this.userDAO.getUserFromDatabase(userId)).thenReturn(Optional.empty());

        //Act
        ApiResponse actualResponse = this.userController.getUser(userId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.userDAO, times(1)).getUserFromDatabase(userId);
    }
}
