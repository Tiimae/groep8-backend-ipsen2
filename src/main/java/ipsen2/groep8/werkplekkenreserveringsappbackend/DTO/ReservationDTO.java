package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ReservationDTO {
    @NotEmpty(message = "userId cannot be empty")
    private String userId;

    private Boolean status;
    private String note;
    private Integer amount;

    // unix timestamp in seconds
    @Min(0)
    @NotNull(message = "starttime must exist")
    private long starttime;
    @Min(0)
    @NotNull(message = "endtime must exist")
    private long endtime;

    @NotNull(message = "Type must exist")
    private String type;

    private String[] meetingRoomIds;
    private String wingId;
}
