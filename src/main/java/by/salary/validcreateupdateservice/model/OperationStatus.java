package by.salary.validcreateupdateservice.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class OperationStatus {
    private OperationStatusCode operationStatusCode;
    private List<ErrorInfo> errors = new ArrayList<>();

    public OperationStatus(OperationStatusCode operationStatusCode, HttpStatus status, String message) {
        this.operationStatusCode = operationStatusCode;
        errors.add(new ErrorInfo(status, message));
    }
}
