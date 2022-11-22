package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.PermissionDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.PermissionMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionControllerTest {
    private PermissionController permissionController;

    @Mock private PermissionDAO permissionDAO;
    @Mock private PermissionMapper permissionMapper;

    @Before
    public void setup() {
        this.permissionController = new PermissionController(this.permissionDAO, this.permissionMapper);
    }

    @Test
    public void should_return404StatusCode_when_permissionWithIdOneDoesNotExists() {
        //Arrange
        String permissionId = "1";
        ApiResponseService expectedResult = new ApiResponseService(HttpStatus.NOT_FOUND, "Permission not found!");

        when(this.permissionDAO.getPermissionFromDatabase(permissionId)).thenReturn(java.util.Optional.empty());

        //Act
        ApiResponseService actualResponse = this.permissionController.getPermission(permissionId);

        //Assert
        org.junit.Assert.assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.permissionDAO, times(1)).getPermissionFromDatabase(permissionId);
    }

    @Test
    public void should_return200StatusCode_when_permissionWithIdOneExists() {
        //Arrange
        String permissionId = "1";
        ApiResponseService expectedResult = new ApiResponseService(HttpStatus.OK, "Permission found!");

        when(this.permissionDAO.getPermissionFromDatabase(permissionId)).thenReturn(java.util.Optional.of(new Permission()));

        //Act
        ApiResponseService actualResponse = this.permissionController.getPermission(permissionId);

        //Assert
        org.junit.Assert.assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.permissionDAO, times(1)).getPermissionFromDatabase(permissionId);
    }

}
