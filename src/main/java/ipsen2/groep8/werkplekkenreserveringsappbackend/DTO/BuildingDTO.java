package ipsen2.groep8.werkplekkenreserveringsappbackend.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Getter
@Setter
public class BuildingDTO {

    private String name;
    private String address;
    private String zipcode;
    private String city;

    private String[] wingIds;

}
