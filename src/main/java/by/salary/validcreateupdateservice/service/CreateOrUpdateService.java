package by.salary.validcreateupdateservice.service;

import by.salary.validcreateupdateservice.accessors.CreateOrUpdateAccessor;
import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import by.salary.validcreateupdateservice.entity.EntityData;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.object.CreateOrUpdateEntity;
import by.salary.validcreateupdateservice.model.object.RequestData;
import by.salary.validcreateupdateservice.model.object.RequestDataRelation;
import by.salary.validcreateupdateservice.model.object.ServiceData;
import by.salary.validcreateupdateservice.model.request.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.request.RelationDto;
import by.salary.validcreateupdateservice.model.response.CreateOrUpdateEntityRelationsResponse;
import by.salary.validcreateupdateservice.model.response.OperationStatusCode;
import by.salary.validcreateupdateservice.validators.CreateOrUpdateEntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateService {

    private final CreateOrUpdateEntityValidator createOrUpdateEntityValidator;
    private final CreateOrUpdateAccessor createOrUpdateAccessor;
    private final String dataSchemaTypeName = "validateInstance";


    public CreateOrUpdateEntityRelationsResponse createOrUpdate(CreateOrUpdateEntityRelationsRequest request) {

        DataSchemaConfiguration dataSchemaConfiguration;
        try {
            //validation
            dataSchemaConfiguration = createOrUpdateAccessor.findAndValidateConfiguration(request);
            CtDataSchemaType ctDataSchemaType = createOrUpdateAccessor.findCtDataSchemaType(dataSchemaTypeName);
            DataSchema dataSchema = createOrUpdateAccessor.findDataSchema(dataSchemaConfiguration, ctDataSchemaType);
            createOrUpdateEntityValidator.validate(request, dataSchema);

            //creating object, data instance
            CreateOrUpdateEntity createOrUpdateEntity = createCreateOrUpdateEntity(request, dataSchemaConfiguration);

            //saving/update by relations

            if (createOrUpdateEntity.getServiceData().getEntityDataId() == null) {
                createOrUpdateAccessor.saveDataEntity(createOrUpdateEntity);
            } else {
                createOrUpdateAccessor.updateDataEntity(createOrUpdateEntity);
            }

        } catch (CreateOrUpdateEntityRelationsException exception) {
            return new CreateOrUpdateEntityRelationsResponse(exception);
        }

        return new CreateOrUpdateEntityRelationsResponse(OperationStatusCode.SUCCESS);
    }

    private CreateOrUpdateEntity createCreateOrUpdateEntity(CreateOrUpdateEntityRelationsRequest request, DataSchemaConfiguration dataSchemaConfiguration) {

        CreateOrUpdateEntity createOrUpdateEntity = new CreateOrUpdateEntity();
        createOrUpdateEntity.setRequestData(new RequestData());
        createOrUpdateEntity.setServiceData(new ServiceData());

        createOrUpdateEntity.getRequestData().setSourceEntityId(request.getSourceEntityId());
        createOrUpdateEntity.getRequestData().setTargetEntityId(request.getTargetEntityId());
        createOrUpdateEntity.getRequestData().setDataSchemaConfigurationId(dataSchemaConfiguration.getId());

        for (RelationDto relationDto : request.getData().getRelations()) {
            Integer entityRelationTypeId = createOrUpdateAccessor
                    .findEntityRelationType(relationDto.getEntityRelationTypeCode())
                    .getId();

            for (UUID targetEntityId : relationDto.getTargetEntityIdList()) {
                createOrUpdateEntity.getRequestData().getRelations()
                        .add(
                                new RequestDataRelation(
                                        targetEntityId,
                                        entityRelationTypeId
                                )
                        );
            }
        }

        EntityData entityData = createOrUpdateAccessor.findDataEntityByTargetAndSource(
                request.getTargetEntityId(),
                request.getSourceEntityId()
        );

        if (entityData != null) {
            createOrUpdateEntity.getServiceData().setEntityDataId(entityData.getId());

            createOrUpdateEntity.getServiceData().setRelations(
                    createOrUpdateAccessor.findAllRelationsByEntityId(
                            createOrUpdateEntity.getServiceData().getEntityDataId()
                    )
            );
        }

        return createOrUpdateEntity;
    }
}
