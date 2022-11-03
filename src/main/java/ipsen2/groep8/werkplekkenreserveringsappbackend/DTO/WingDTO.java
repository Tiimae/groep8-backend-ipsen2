package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WingDTO {

    @NotNull(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Workplaces can't be empty")
    private Long workplaces;
    @NotNull(message = "Floor can't be empty")
    private Long floor;

    private String[] departmentIds;
    private String buildingId;
    private String[] reservationIds;
    private String[] meetingRoomIds;
}
