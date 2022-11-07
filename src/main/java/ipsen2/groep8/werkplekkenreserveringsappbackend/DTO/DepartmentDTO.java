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
public class DepartmentDTO {
    @NotNull(message = "Department name can't be null")
    private String name;

    private String[] userIds;
    private String[] wingIds;
}

