package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shad Rahim
 * @version 1.0
 */
@Getter
@Setter
public class PermissionDTO {

    private String id;
    private String name;

    private String[] roleIds;
}

