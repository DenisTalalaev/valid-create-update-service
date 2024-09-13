package by.salary.validcreateupdateservice.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
public class OperationStatus {
    private OperationStatusCode operationStatusCode;
    private List<ErrorInfo> errors = new ArrayList<>();

    public OperationStatus(OperationStatusCode operationStatusCode, HttpStatus status, String message) {
        this.operationStatusCode = operationStatusCode;
        errors.add(new ErrorInfo(status, message));
    }

    public OperationStatus(OperationStatusCode operationStatusCode) {
        this.operationStatusCode = operationStatusCode;
    }
}
