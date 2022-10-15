package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/reservation")
@Validated
public class ReservationController {
    private final ReservationDAO reservationDAO;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationDAO reservationDAO, ReservationMapper reservationMapper) {
        this.reservationDAO = reservationDAO;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String id) throws EntryNotFoundException {
        Optional<Reservation> reservationEntry = this.reservationDAO.getReservationFromDatabase(id);
        if (reservationEntry.isEmpty()) throw new EntryNotFoundException("Reservation not found");
        return new ResponseEntity<>(reservationEntry.get(), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<Reservation> getReservations() {
        return this.reservationDAO.getAllReservations();
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Reservation> postReservation(@RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.saveReservationToDatabase(reservation);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @PutMapping(value ="/{id}", consumes = {"application/json"})
    public ResponseEntity<Reservation> updateReservation(@PathVariable String id, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.updateReservationInDatabase(id, reservation);
        return new ResponseEntity<>(reservation, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void deleteReservation(@PathVariable String id) {
        this.reservationDAO.deleteReservationFromDatabase(id);
    }

    @GetMapping(value = "/{id}/user")
    public ResponseEntity<User> getReservationUser(@PathVariable String id) throws EntryNotFoundException {
        Optional<Reservation> reservationEntry = this.reservationDAO.getReservationFromDatabase(id);

        if (reservationEntry.isEmpty()) throw new EntryNotFoundException("Reservation not found");

        Reservation presentReservation = reservationEntry.get();
        return new ResponseEntity<>(presentReservation.getUser(), HttpStatus.OK);
    }
}
