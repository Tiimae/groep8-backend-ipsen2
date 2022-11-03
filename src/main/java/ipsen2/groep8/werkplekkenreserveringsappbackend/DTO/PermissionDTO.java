package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO {

    private String id;
    private String name;

    private String[] roleIds;
}

