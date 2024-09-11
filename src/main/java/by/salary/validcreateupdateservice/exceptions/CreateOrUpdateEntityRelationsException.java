package by.salary.validcreateupdateservice.exceptions;

import org.springframework.http.HttpStatus;

public class CreateOrUpdateEntityRelationsException extends AbstractException {

    private final HttpStatus status;

    public CreateOrUpdateEntityRelationsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
