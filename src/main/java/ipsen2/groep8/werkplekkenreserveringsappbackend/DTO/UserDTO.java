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
public class UserDTO {

    @NotNull(message = "There must be a name")
    private String name;

    @NotNull(message = "There must be a email")
    private String email;

    @NotNull(message = "There must be a password")
    private String password;

    private Boolean verified;
    private Boolean resetRequired;

    private String departmentId;
    private String[] roleIds;
    private String[] reservationIds;
}
