package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.WingMapper;
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
public class WingControllerTest {

    private WingController wingController;

    @Mock private WingDAO wingDAO;
    @Mock private WingMapper wingMapper;

    @Before
    public void setup() {
        this.wingController = new WingController(this.wingDAO, this.wingMapper);
    }

    @Test
    public void should_return404StatusCode_when_wingWithIdOneDoesNotExists() {
        //Arrange
        String wingId = "1";
        ApiResponse expectedResult = new ApiResponse(HttpStatus.NOT_FOUND, "The wing has not been found!");

        when(this.wingDAO.getWingFromDatabase(wingId)).thenReturn(Optional.empty());

        //Act
        ApiResponse actualResponse = this.wingController.getWing(wingId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.wingDAO, times(1)).getWingFromDatabase(wingId);
    }
    

}
