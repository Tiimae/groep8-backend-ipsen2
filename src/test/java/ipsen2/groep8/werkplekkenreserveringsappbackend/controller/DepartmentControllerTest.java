package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
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
public class DepartmentControllerTest {

    private DepartmentController departmentController;

    @Mock private DepartmentDAO departmentDAO;
    @Mock private DepartmentMapper departmentMapper;

    @Before
    public void setup() {
        this.departmentController = new DepartmentController(this.departmentDAO, this.departmentMapper);
    }

    @Test
    public void should_return404StatusCode_when_depratmentWithIdOneDoesNotExists() {
        //Arrange
        String departmentId = "1";
        ApiResponseService expectedResult = new ApiResponseService(HttpStatus.NOT_FOUND, "Department not found!");

        when(this.departmentDAO.getDepartmentFromDatabase(departmentId)).thenReturn(Optional.empty());

        //Act
        ApiResponseService actualResponse = this.departmentController.getDepartment(departmentId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.departmentDAO, times(1)).getDepartmentFromDatabase(departmentId);
    }

}
