package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * @author Tim de Kok
 * @version 1.0
 */

@Getter
public class ApiResponseService<Type> {
    private final long code;
    private Type payload;
    private final String message;

    public ApiResponseService(HttpStatus code, Type payload) {
        this.code = code.value();
        this.message = code.getReasonPhrase();
        this.payload = payload;
    }

    public ApiResponseService(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }
}
