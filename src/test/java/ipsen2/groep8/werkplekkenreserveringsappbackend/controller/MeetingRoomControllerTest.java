package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.MeetingRoomDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.MeetingRoomMapper;
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
public class MeetingRoomControllerTest {

    private MeetingRoomController meetingRoomController;

    @Mock private MeetingRoomDAO meetingRoomDAO;
    @Mock private MeetingRoomMapper meetingRoomMapper;

    @Before
    public void setup() {
        this.meetingRoomController = new MeetingRoomController(this.meetingRoomDAO, this.meetingRoomMapper);
    }

    @Test
    public void should_return404StatusCode_when_meetingRoomWithIdOneDoesNotExists() {
        //Arrange
        String meetingRoomId = "1";
        ApiResponse expectedResult = new ApiResponse(HttpStatus.NOT_FOUND, "Meeting room has not been found");

        when(this.meetingRoomDAO.getMeetingRoomFromDatabase(meetingRoomId)).thenReturn(Optional.empty());

        //Act
        ApiResponse actualResponse = this.meetingRoomController.getMeetingRoom(meetingRoomId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.meetingRoomDAO, times(1)).getMeetingRoomFromDatabase(meetingRoomId);
    }
    

}
