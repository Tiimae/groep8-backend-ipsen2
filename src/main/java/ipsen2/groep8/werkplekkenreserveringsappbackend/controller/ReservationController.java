package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/reservation")
@Validated
public class ReservationController {
    private final EmailService emailService;
    private final ReservationDAO reservationDAO;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationDAO reservationDAO, ReservationMapper reservationMapper, EmailService emailService) {
        this.reservationDAO = reservationDAO;
        this.reservationMapper = reservationMapper;
        this.emailService = emailService;
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<Optional<Reservation>> getReservation(@PathVariable String reservationId) {
        Optional<Reservation> reservation = this.reservationDAO.getReservationFromDatabase(reservationId);

        if (reservation.isEmpty()){
            return new ApiResponse(HttpStatus.NOT_FOUND, "Reservation not found!");
        }

        return new ApiResponse(HttpStatus.OK, reservation);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponse<List<Reservation>> getReservations() {
        List<Reservation> allReservations = this.reservationDAO.getAllReservations();
        return new ApiResponse(HttpStatus.ACCEPTED, allReservations);
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Reservation> postReservation(@RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.saveReservationToDatabase(reservation);

        try {
             List<String> meetingRooms = new ArrayList<>();
             reservation.getMeetingRooms().stream().forEach(meetingRoom -> {
                meetingRooms.add(meetingRoom.getId());
            });

            this.emailService.sendMessage(reservation.getUser().getEmail(), "reservation has been placed", "you made an reservation in wing " + reservation.getWing().getName() + " in meetingrooms: " + String.join(meetingRooms.stream().collect(Collectors.joining(","))));
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

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
    public ApiResponse deleteReservation(@PathVariable String id) {
        this.reservationDAO.deleteReservationFromDatabase(id);
        return new ApiResponse(HttpStatus.ACCEPTED, "Reservation has been deleted");
    }

    @GetMapping(value = "/{id}/user")
    public ResponseEntity<User> getReservationUser(@PathVariable String id) throws EntryNotFoundException {
        Optional<Reservation> reservationEntry = this.reservationDAO.getReservationFromDatabase(id);

        if (reservationEntry.isEmpty()) throw new EntryNotFoundException("Reservation not found");

        Reservation presentReservation = reservationEntry.get();
        return new ResponseEntity<>(presentReservation.getUser(), HttpStatus.OK);
    }
}
