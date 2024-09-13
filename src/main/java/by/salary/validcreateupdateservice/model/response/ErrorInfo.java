package by.salary.validcreateupdateservice.model.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorInfo {
    Object target = null;
    HttpStatus errorCode;
    String errorDetail;

    public ErrorInfo(HttpStatus status, String message) {
        this.errorCode = status;
        this.errorDetail = message;
    }
}
