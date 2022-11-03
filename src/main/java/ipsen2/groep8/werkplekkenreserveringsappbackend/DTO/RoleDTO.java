package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoleDTO {

    @NotNull(message = "There must be a role name")
    private String roleName;

    private String[] userIds;
    private String[] permissionIds;

}