package by.salary.validcreateupdateservice.model;

import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import lombok.Getter;

@Getter
public class CreateOrUpdateEntityRelationsResponse {

    private final OperationStatus operationStatus;

    public CreateOrUpdateEntityRelationsResponse(CreateOrUpdateEntityRelationsException exception) {
        operationStatus = new OperationStatus(
                OperationStatusCode.ERROR,
                exception.getStatus(),
                exception.getMessage()
        );
    }
}
