package by.salary.validcreateupdateservice.service;

import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsResponse;
import by.salary.validcreateupdateservice.validators.CreateOrUpdateEntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateService {

    private final CreateOrUpdateEntityValidator createOrUpdateEntityValidator;

    public CreateOrUpdateEntityRelationsResponse createOrUpdate(CreateOrUpdateEntityRelationsRequest request){
        try {
            createOrUpdateEntityValidator.validate(request);
        } catch (CreateOrUpdateEntityRelationsException exception) {
            return new CreateOrUpdateEntityRelationsResponse(exception);
        }

        // TODO: create or update
        return null;
    }
}
