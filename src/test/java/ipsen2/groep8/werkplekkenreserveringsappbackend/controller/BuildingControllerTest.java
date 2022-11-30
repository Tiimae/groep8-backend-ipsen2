package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.BuildingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuildingControllerTest {
    private BuildingController buildingController;

    @Mock private BuildingDAO buildingDAO;
    @Mock private BuildingMapper buildingMapper;

    @Before
    public void setup() {
        this.buildingController = new BuildingController(this.buildingDAO, this.buildingMapper);
    }

    @Test
    public void should_return404StatusCode_when_buildingWithIdOneDoesNotExists() {
        //Arrange
        String buildingId = "1";
        ApiResponseService expectedResult = new ApiResponseService(HttpStatus.NOT_FOUND, "Building not found!");

        when(this.buildingDAO.getBuildingFromDatabase(buildingId)).thenReturn(java.util.Optional.empty());

        //Act
        ApiResponseService actualResponse = this.buildingController.getBuilding(buildingId);

        //Assert
        org.junit.Assert.assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.buildingDAO, times(1)).getBuildingFromDatabase(buildingId);
    }
}
