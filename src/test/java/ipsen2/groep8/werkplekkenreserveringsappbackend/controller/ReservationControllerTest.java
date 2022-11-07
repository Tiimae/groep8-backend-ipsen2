package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationControllerTest {

    private ReservationController reservationController;

    @Mock private ReservationDAO reservationDAO;
    @Mock private ReservationMapper reservationMapper;
    @Mock private EmailService emailService;

    @Before
    public void setup() {
        this.reservationController = new ReservationController(this.reservationDAO, this.reservationMapper, this.emailService);
    }

    @Test
    public void should_return404StatusCode_when_reservationWithIdOneDoesNotExists() {
        //Arrange
        String reservationId = "1";
        ApiResponse expectedResult = new ApiResponse(HttpStatus.NOT_FOUND, "Reservation not found!");

        when(this.reservationDAO.getReservationFromDatabase(reservationId)).thenReturn(Optional.empty());

        //Act
        ApiResponse actualResponse = this.reservationController.getReservation(reservationId);

        //Assert
        assertEquals(expectedResult.getCode(), actualResponse.getCode());

        verify(this.reservationDAO, times(1)).getReservationFromDatabase(reservationId);
    }


}
