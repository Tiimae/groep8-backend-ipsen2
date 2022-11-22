package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @ResponseBody
    public ApiResponseService<Optional<Reservation>> getReservation(@PathVariable String id) {
        Optional<Reservation> reservation = this.reservationDAO.getReservationFromDatabase(id);

        if (reservation.isEmpty()){
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Reservation not found!");
        }

        return new ApiResponseService(HttpStatus.OK, reservation);
    }



    @GetMapping(value = "")
    @ResponseBody
    public ApiResponseService<List<Reservation>> getReservations(@RequestParam(required = false) String filter) {
        List<Reservation> allReservations = this.reservationDAO.getAllReservations();
        Set<Reservation> filteredReservations = new HashSet<>();


        if(filter != null && filter.equals("thismonth")){
            for (Reservation reservation : allReservations) {
                if(LocalDateTime.now().getMonth() == reservation.getStartDate().getMonth()){
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        if(filter != null && filter.equals("lastmonth")){
            for (Reservation reservation : allReservations) {
                if(LocalDateTime.now().minusMonths(1).getMonth() == reservation.getStartDate().getMonth()){
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, allReservations);
    }

    @PostMapping(consumes = {"application/json"})
    public ApiResponseService<Reservation> postReservation(@RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.saveReservationToDatabase(reservation);

        try {
             List<String> meetingRooms = new ArrayList<>();
             reservation.getMeetingRooms().stream().forEach(meetingRoom -> {
                meetingRooms.add(meetingRoom.getId());
            });

            this.emailService.sendMessage(
                reservation.getUser().getEmail(),
                "Your CGI reservation",
                "<p>Hi, we have received your reservation for a " + reservation.getType() + ", and confirm your booking.</p><p style=\"margin: 0;\">" + reservation.getStartDate().format(DateTimeFormatter.ofPattern("d MMMM y")) + "</p>" + reservation.getStartDate().toLocalTime() + " - " + reservation.getEndDate().toLocalTime()
            );
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        return new ApiResponseService<>(HttpStatus.CREATED, reservation);
    }

    @PutMapping(value ="/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateReservation(@PathVariable String id, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.updateReservationInDatabase(id, reservation);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Reservation has been updated");
    }

    @PatchMapping(value ="/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService patchReservation(@PathVariable String id, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.updateReservationInDatabase(id, reservation);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Reservation has been updated");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ApiResponseService deleteReservation(@PathVariable String id) {
        this.reservationDAO.deleteReservationFromDatabase(id);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Reservation has been deleted");
    }

    @GetMapping(value = "/{id}/user")
    public ApiResponseService<User> getReservationUser(@PathVariable String id) throws EntryNotFoundException {
        Optional<Reservation> reservationEntry = this.reservationDAO.getReservationFromDatabase(id);

        if (reservationEntry.isEmpty()) throw new EntryNotFoundException("Reservation not found");

        Reservation presentReservation = reservationEntry.get();
        return new ApiResponseService<>(HttpStatus.OK, presentReservation.getUser());
    }
}
