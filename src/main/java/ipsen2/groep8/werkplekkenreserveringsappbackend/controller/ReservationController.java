package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/reservation")
public class ReservationController {
    private final ReservationDAO reservationDAO;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationDAO reservationDAO, ReservationMapper reservationMapper) {
        this.reservationDAO = reservationDAO;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Optional<Reservation> getReservation(@PathVariable String id) {
        return this.reservationDAO.getReservationFromDatabase(id);
    }

    @GetMapping(value = "")
    @ResponseBody
    public List<Reservation> getReservations() {
        return this.reservationDAO.getAllReservations();
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation postReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.saveReservationToDatabase(reservation);
        return reservation;
    }

    @PutMapping(value = "", consumes = {"apllication/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    //TODO: move to DTO
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        this.reservationDAO.updateReservationInDatabase(reservation);
        return reservation;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteReservation(@PathVariable String id) {
        this.reservationDAO.deleteReservationFromDatabase(id);
        return id;
    }

    @GetMapping(value = "/{id}/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public User getReservationUser(@PathVariable String id) {
        Optional<Reservation> reservation = this.reservationDAO.getReservationFromDatabase(id);

        if (reservation.isPresent()) {
            Reservation presentReservation = reservation.get();
            return presentReservation.getUser();
        }

        return null;
    }
}
