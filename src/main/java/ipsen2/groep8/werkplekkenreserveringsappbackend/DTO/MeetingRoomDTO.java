package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Getter
@Setter
public class MeetingRoomDTO {
    @NotNull(message = "Must be an amount of people")
    private Long amountPeople;

    private String wingId;
    private String[] reservationIds;
}
