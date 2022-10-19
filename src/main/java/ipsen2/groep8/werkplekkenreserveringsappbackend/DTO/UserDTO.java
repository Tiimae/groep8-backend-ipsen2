package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {

    @NotNull(message = "There must be a name")
    private String name;

    @NotNull(message = "There must be a email")
    private String email;

    @NotNull(message = "There must be a password")
    private String password;

    private String departmentId;
    private String[] roleIds;
    private String[] reservationIds;
}
