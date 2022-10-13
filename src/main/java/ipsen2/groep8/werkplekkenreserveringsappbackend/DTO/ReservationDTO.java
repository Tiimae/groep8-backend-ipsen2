package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationDTO {
    private String userId;

    private boolean status;
    private String note;
    private int amount;

    private int starttime;
    private int endtime;

    private String[] meetingRoomIds;
    private String wingId;
}
