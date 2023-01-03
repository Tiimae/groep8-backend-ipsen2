package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        if (reservation.isEmpty()){
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Reservation not found!");
        }

        return new ApiResponseService(HttpStatus.FOUND, reservation);
    }



    @GetMapping(value = ApiConstant.getAllReservations)
    @ResponseBody
    public ApiResponseService<List<Reservation>> getReservations(@RequestParam(required = false) String filter) {
        List<Reservation> allReservations = this.reservationDAO.getAllReservations();
        Set<Reservation> filteredReservations = new HashSet<>();

        ////////////////////////
        LocalDateTime date = LocalDateTime.now();
        for (Reservation reservation : allReservations) {
            LocalDateTime time = reservation.getEndDate();
            if(!date.toLocalDate().isEqual(time.toLocalDate())){
                continue;
            }
            LocalTime currentTime = date.toLocalTime();
            LocalTime endTime = time.toLocalTime();

            // Is op dezelfde dag, en huidige tijd is meer dan eindtijd (= expired).
            if(currentTime.toSecondOfDay() > endTime.toSecondOfDay()){
                System.out.println("Current time is: " + date + " - " + "Reservation endDate is: " + reservation.getEndDate());
//                Thread newThread = new Thread(() -> {
//                    try {
//                        this.emailService.sendMessage(
//                                reservation.getUser().getEmail(),
//                                "Your CGI reservation",
//                                "<p>Hi, you have forgotten to checkin or show up on ur your reservation for a " + reservation.getType() + ", .</p><p style=\"margin: 0;\">" + reservation.getStartDate().format(DateTimeFormatter.ofPattern("d MMMM y")) + "</p>" + reservation.getStartDate().toLocalTime() + " - " + reservation.getEndDate().toLocalTime()
//                        );
//
//                    } catch (MessagingException e) {
//                        throw new RuntimeException(e);
//                        }
//
//                });
//                newThread.start();

            }
        }



            // Checks if reservation is expired op current day of reservation AND na endTime of reservation.
//            if(date.toLocalDate().isEqual(time.toLocalDate()) && currentTime.toSecondOfDay()  > endTime.toSecondOfDay() ){ // Current en Endtime moeten andersom
//                long diffInSeconds = currentTime.toSecondOfDay() - endTime.toSecondOfDay(); // Moet zijn currentTime - endTime
//                double diffInMinutes = Math.floor(diffInSeconds / 60);
//
//                // Every 2 minutes AND current day being reservation day trigger
//                if(diffInMinutes % 2 == 0){
//                    System.out.println("Current time is: " + currentTime + " - " + "Res endtime is: " + endTime + " - " + "Overgebleven in sec is: " + diffInMinutes + " - " + "Aantal min verschil is: " + diffInMinutes);
//                }
//
//            }

        //////////////////////////

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

    @PostMapping(value = ApiConstant.getAllReservations, consumes = {"application/json"})
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

    @PutMapping(value =ApiConstant.getReservation, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateReservation(@PathVariable String reservationId, @RequestBody @Valid ReservationDTO reservationDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.reservationDAO.updateReservationInDatabase(reservationId, reservationDTO));
    }

    @PatchMapping(value =ApiConstant.getReservation, consumes = {"application/json"})
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


//                    try {
//                        this.emailService.sendMessage(
//                                reservation.getUser().getEmail(),
//                                "Your CGI reservation",
//                                "<p>Hi, you have forgotten to checkin or show up on ur your reservation for a " + reservation.getType() + ", .</p><p style=\"margin: 0;\">" + reservation.getStartDate().format(DateTimeFormatter.ofPattern("d MMMM y")) + "</p>" + reservation.getStartDate().toLocalTime() + " - " + reservation.getEndDate().toLocalTime()
//                        );
//                    } catch (MessagingException e) {
//                        System.out.println(e.getMessage());
//                    }