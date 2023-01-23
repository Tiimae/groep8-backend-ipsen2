package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.thread.MailThread;

@RestController
@RequestMapping(

)
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

    @GetMapping(value = ApiConstant.getReservation)
    @ResponseBody
    public ApiResponseService<Optional<Reservation>> getReservation(@PathVariable String reservationId) {
        Optional<Reservation> reservation = this.reservationDAO.getReservationFromDatabase(reservationId);

        if (reservation.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Reservation not found!");
        }

        return new ApiResponseService(HttpStatus.FOUND, reservation);
    }


    @GetMapping(value = ApiConstant.getAllReservations)
    @ResponseBody
    public ApiResponseService<List<Reservation>> getReservations(@RequestParam(required = false) String filter) {
        List<Reservation> allReservations = this.reservationDAO.getAllReservations();
        Set<Reservation> filteredReservations = new HashSet<>();


        if (filter != null && filter.equals("thismonth")) {
            for (Reservation reservation : allReservations) {
                if (LocalDateTime.now().getMonth() == reservation.getStartDate().getMonth()) {
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        if (filter != null && filter.equals("lastmonth")) {
            for (Reservation reservation : allReservations) {
                if (LocalDateTime.now().minusMonths(1).getMonth() == reservation.getStartDate().getMonth()) {
                    filteredReservations.add(reservation);
                }
            }
            return new ApiResponseService(HttpStatus.OK, filteredReservations);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, allReservations);
    }

    @PostMapping(value = ApiConstant.getAllReservations, consumes = {"application/json"})
    public ApiResponseService<Reservation> postReservation(@RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        Reservation reservation = reservationMapper.toReservation(reservationDTO);
        this.reservationDAO.saveReservationToDatabase(reservation);
        List<String> meetingRooms = new ArrayList<>();
        reservation.getMeetingRooms().stream().forEach(meetingRoom -> {
            meetingRooms.add(meetingRoom.getId());
        });

        MailThread mail = new MailThread(
                reservation.getUser().getEmail(),
                "Your CGI reservation",
                "<p>Hi, we have received your reservation for a " + reservation.getType() + ", and confirm your booking.</p><p style=\"margin: 0;\">" + reservation.getStartDate().format(DateTimeFormatter.ofPattern("d MMMM y")) + "</p>" + reservation.getStartDate().toLocalTime() + " - " + reservation.getEndDate().toLocalTime(),
                this.emailService
        );
        mail.start();

        return new ApiResponseService<>(HttpStatus.CREATED, reservation);
    }

    @PutMapping(value = ApiConstant.getReservation, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateReservation(@PathVariable String reservationId, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.reservationDAO.updateReservationInDatabase(reservationId, reservationDTO));
    }

    @PatchMapping(value = ApiConstant.getReservation, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService patchReservation(@PathVariable String reservationId, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.reservationDAO.updateReservationInDatabase(reservationId, reservationDTO));
    }

    @DeleteMapping(value = ApiConstant.getReservation)
    @ResponseBody
    public ApiResponseService deleteReservation(@PathVariable String reservationId) {
        this.reservationDAO.deleteReservationFromDatabase(reservationId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Reservation has been deleted");
    }

    @GetMapping(value = ApiConstant.getReservationUser)
    public ApiResponseService<User> getReservationUser(@PathVariable String reservationId) throws EntryNotFoundException {
        Optional<Reservation> reservationEntry = this.reservationDAO.getReservationFromDatabase(reservationId);

        if (reservationEntry.isEmpty()) throw new EntryNotFoundException("Reservation not found");

        Reservation presentReservation = reservationEntry.get();
        return new ApiResponseService<>(HttpStatus.OK, presentReservation.getUser());
    }
}
