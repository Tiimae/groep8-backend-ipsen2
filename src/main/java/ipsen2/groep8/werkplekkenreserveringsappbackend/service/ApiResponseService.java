package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import org.springframework.http.HttpStatus;

public class ApiResponseService<Type> {
    private HttpStatus code;
    private Type payload;
    private String message;

    public ApiResponseService(HttpStatus code, Type payload) {
        this.code = code;
        this.payload = payload;
    }

    public ApiResponseService(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public Type getPayload() {
        return payload;
    }

    public void setPayload(Type payload) {
        this.payload = payload;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
