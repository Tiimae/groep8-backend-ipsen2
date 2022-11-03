package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.UserMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
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
public class RoleControllerTest {

    @Mock private RoleDAO roleDAO;
    @Mock private RoleMapper roleMapper;

    private RoleController roleController;

    @Before
    public void setup() {
        this.roleController = new RoleController(this.roleDAO, this.roleMapper);
    }

    @Test
    public void should_return404StatusCode_when_roleWithIdOneDoesNotExists() {
        //Arrange
        String roleId = "1";
        ApiResponse expectedResult = new ApiResponse(HttpStatus.NOT_FOUND, "The role has not been found");

        when(this.roleDAO.getRoleFromDatabase(roleId)).thenReturn(Optional.empty());

        //Act
        ApiResponse actualResponse = this.roleController.getRole(roleId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.roleDAO, times(1)).getRoleFromDatabase(roleId);
    }

}
