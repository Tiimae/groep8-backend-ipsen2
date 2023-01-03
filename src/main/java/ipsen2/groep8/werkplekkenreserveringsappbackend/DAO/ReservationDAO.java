package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.ReservationRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.ReservationDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.ReservationMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.MeetingRoom;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationDAO {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper mapper;

    public ReservationDAO(ReservationRepository reservationRepository, @Lazy ReservationMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.mapper = mapper;
    }

    public Optional<Reservation> getReservationFromDatabase(String id) {
        return this.reservationRepository.findById(id);
    }

    public List<Reservation> getAllReservations() {
//        this.findAllReservations();
        return this.reservationRepository.findAll();
    }

    public void findAllReservations(){
        List<Reservation> allReservations = this.reservationRepository.findAll();
        LocalDateTime date = LocalDateTime.now();
        for (Reservation reservation : allReservations) {
            LocalDateTime time = reservation.getEndDate();
            LocalTime currentTime = date.toLocalTime();
            LocalTime endTime = time.toLocalTime();
            if(date.toLocalDate().isEqual(time.toLocalDate()) && endTime.toSecondOfDay()  > currentTime.toSecondOfDay() ){ // Current en Endtime moeten andersom


                long diffInSeconds = endTime.toSecondOfDay() - currentTime.toSecondOfDay(); // Moet zijn currentTime - endTime

                double diffInMinutes = Math.floor(diffInSeconds / 60);

                // Every 2 minutes AND current day being reservation day trigger
                if(diffInMinutes % 2 == 0 && date.toLocalDate().isEqual(time.toLocalDate())){
                    System.out.println("Current time is: " + currentTime + " - " + "Res endtime is: " + endTime + " - " + "Overgebleven in sec is: " + diffInMinutes + " - " + "Aantal min verschil is: " + diffInMinutes);
                }

            }
        }

    }

    public void saveReservationToDatabase(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    public Reservation updateReservationInDatabase(String id, ReservationDTO updateReservation) throws EntryNotFoundException {
        Reservation reservation = this.reservationRepository.getById(id);
        mapper.mergeReservations(reservation, updateReservation);
        return this.reservationRepository.saveAndFlush(reservation);
    }

    public void deleteReservationFromDatabase(String id) {
        final Reservation reservation = this.reservationRepository.getById(id);

        reservation.getUser().getReservations().remove(reservation);
        reservation.setUser(null);

        reservation.getWing().getReservations().remove(reservation);
        reservation.setWing(null);

        for (MeetingRoom meetingRoom : reservation.getMeetingRooms()) {
            reservation.getMeetingRooms().remove(meetingRoom);
            meetingRoom.getReservations().remove(reservation);
        }

        this.reservationRepository.deleteById(id);
    }
}
