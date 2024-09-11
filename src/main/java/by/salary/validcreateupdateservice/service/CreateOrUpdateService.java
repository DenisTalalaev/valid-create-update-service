package by.salary.validcreateupdateservice.service;

import by.salary.validcreateupdateservice.accessors.CreateOrUpdateAccessor;
import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import by.salary.validcreateupdateservice.entity.EntityData;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsResponse;
import by.salary.validcreateupdateservice.repository.DataSchemaConfigurationRepository;
import by.salary.validcreateupdateservice.validators.CreateOrUpdateEntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateService {

    private final CreateOrUpdateEntityValidator createOrUpdateEntityValidator;
    private final CreateOrUpdateAccessor createOrUpdateAccessor;
    private final String dataSchemaTypeName = "validateInstance";


    public CreateOrUpdateEntityRelationsResponse createOrUpdate(CreateOrUpdateEntityRelationsRequest request) {

        DataSchemaConfiguration dataSchemaConfiguration;
        try {
            dataSchemaConfiguration = createOrUpdateAccessor.validateConfiguration(request);
            CtDataSchemaType ctDataSchemaType = createOrUpdateAccessor.findCtDataSchemaType(dataSchemaTypeName);
            DataSchema dataSchema = createOrUpdateAccessor.findDataSchema(dataSchemaConfiguration, ctDataSchemaType);
            createOrUpdateEntityValidator.validate(request, dataSchema);
        } catch (CreateOrUpdateEntityRelationsException exception) {
            return new CreateOrUpdateEntityRelationsResponse(exception);
        }

        EntityData entityData = new EntityData();
        entityData.setSourceEntityId(request.getSourceEntityId());
        entityData.setTargetEntityId(request.getTargetEntityId());
        entityData.setDataSchemaConfiguration(dataSchemaConfiguration);


        return null;
    }
}
