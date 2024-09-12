package by.salary.validcreateupdateservice.accessors;

import by.salary.validcreateupdateservice.entity.*;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntity;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.RequestDataRelation;
import by.salary.validcreateupdateservice.model.ServiceDataRelation;
import by.salary.validcreateupdateservice.repository.*;
import com.fasterxml.jackson.annotation.JacksonInject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateAccessor {

    private final DataSchemaConfigurationRepository dataSchemaConfigurationRepository;
    private final DataSchemaRepository dataSchemaRepository;
    private final CtDataSchemaTypeRepository ctDataSchemaTypeRepository;

    private final EntityDataRepository entityDataRepository;
    private final EntityRelationRepository entityRelationRepository;
    private final EntityRelationTypeRepository entityRelationTypeRepository;

    public DataSchema findDataSchema(DataSchemaConfiguration dataSchemaConfiguration, CtDataSchemaType ctDataSchemaType) {
        return dataSchemaRepository.findByDataSchemaConfigurationAndCtDataSchemaType(
                dataSchemaConfiguration,
                ctDataSchemaType
        ).orElseThrow(
                () -> new CreateOrUpdateEntityRelationsException(
                        "Data schema not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    public CtDataSchemaType findCtDataSchemaType(String dataSchemaTypeName) {
        return ctDataSchemaTypeRepository.findByName(dataSchemaTypeName).orElseThrow(
                () -> new CreateOrUpdateEntityRelationsException(
                        "Data schema type with name: " + dataSchemaTypeName + " not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    //
    public DataSchemaConfiguration findAndValidateConfiguration(CreateOrUpdateEntityRelationsRequest request) throws CreateOrUpdateEntityRelationsException {
        UUID dataSchemaConfigurationCode = request.getDataSchemaConfigurationCode();
        if (dataSchemaConfigurationCode == null) {
            throw new CreateOrUpdateEntityRelationsException(
                    "Data schema configuration code is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        List<DataSchemaConfiguration> dataSchemaConfiguration = dataSchemaConfigurationRepository
                .findAllByCode(dataSchemaConfigurationCode);

        if (dataSchemaConfiguration.size() != 1) {
            throw new CreateOrUpdateEntityRelationsException(
                    "Must be one data schema configuration with code: " + dataSchemaConfigurationCode,
                    HttpStatus.BAD_REQUEST
            );
        }

        return dataSchemaConfiguration.get(0);
    }

    public EntityRelationType findEntityRelationType(UUID entityRelationTypeCode) {
        return ctDataSchemaTypeRepository.findByCode(entityRelationTypeCode.toString()).orElseThrow(
                () -> new CreateOrUpdateEntityRelationsException(
                        "Entity relation type with code: " + entityRelationTypeCode + " not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    public EntityData findDataEntityByTargetAndSource(UUID targetEntityId, UUID sourceEntityId) {
        return entityDataRepository.findByTargetEntityIdAndSourceEntityId(targetEntityId, sourceEntityId).orElse(null);
    }

    public List<ServiceDataRelation> findAllRelationsByEntityId(Long entityDataId) {
        return entityRelationRepository.findAllById(entityDataId)
                .stream()
                .map(entityRelation -> new ServiceDataRelation(
                        entityRelation.getId(),
                        entityRelation.getTargetEntityId(),
                        entityRelation.getEntityRelationType().getId()
                ))
                .collect(Collectors.toList());
    }

    public void saveDataEntity(CreateOrUpdateEntity createOrUpdateEntity) {
        EntityData entityData = entityDataRepository.save(
                EntityData.builder()
                        .sourceEntityId(createOrUpdateEntity.getRequestData().getSourceEntityId())
                        .targetEntityId(createOrUpdateEntity.getRequestData().getTargetEntityId())
                        .dataSchemaConfiguration(
                                dataSchemaConfigurationRepository.getById(
                                        createOrUpdateEntity.getRequestData().getDataSchemaConfigurationId()
                                )
                        )
                        .createdDate(new Timestamp(System.currentTimeMillis()))
                        .lastUpdated(new Timestamp(System.currentTimeMillis()))
                        .build()
        );

        for (ServiceDataRelation serviceDataRelation : createOrUpdateEntity.getServiceData().getRelations()) {
            entityRelationRepository.save(
                    EntityRelation.builder()
                            .entityData(entityData)
                            .targetEntityId(serviceDataRelation.getTargetEntityId())
                            .entityRelationType(
                                    entityRelationTypeRepository.getById(
                                            serviceDataRelation.getEntityRelationTypeId()
                                    )
                            )
                            .build()
            );
        }

    }

    public void updateDataEntity(CreateOrUpdateEntity createOrUpdateEntity) {
        // Update EntityData
        EntityData entityData = entityDataRepository.getById(createOrUpdateEntity.getServiceData().getEntityDataId());

        entityData.setTargetEntityId(createOrUpdateEntity.getRequestData().getTargetEntityId() == null?
                entityData.getTargetEntityId() : createOrUpdateEntity.getRequestData().getTargetEntityId());

        entityData.setSourceEntityId(createOrUpdateEntity.getRequestData().getSourceEntityId() == null?
                entityData.getSourceEntityId() : createOrUpdateEntity.getRequestData().getSourceEntityId());

        entityData.setDataSchemaConfiguration(createOrUpdateEntity.getRequestData().getDataSchemaConfigurationId() == null?
                entityData.getDataSchemaConfiguration():
                dataSchemaConfigurationRepository.getById(createOrUpdateEntity.getRequestData().getDataSchemaConfigurationId()));

        entityData.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        entityDataRepository.save(entityData);


        // Find relations
        List<RequestDataRelation> requestRelations = createOrUpdateEntity.getRequestData().getRelations();
        List<ServiceDataRelation> serviceRelations = createOrUpdateEntity.getServiceData().getRelations();

        // Add relations that are not in db
        requestRelations.stream()
                .filter(requestRelation -> serviceRelations.stream()
                        .noneMatch(serviceRelation ->
                                requestRelation.getTargetEntityId().equals(serviceRelation.getTargetEntityId()) &&
                                        requestRelation.getEntityRelationTypeId().equals(serviceRelation.getEntityRelationTypeId())
                        )
                )
                .forEach(requestRelation -> {
                    EntityRelation newRelation = EntityRelation.builder()
                            .entityData(entityData)
                            .targetEntityId(requestRelation.getTargetEntityId())
                            .entityRelationType(entityRelationTypeRepository.getById(requestRelation.getEntityRelationTypeId()))
                            .build();
                    entityRelationRepository.save(newRelation);
                });

        // Delete relations that are not in new version
        serviceRelations.stream()
                .filter(serviceRelation -> requestRelations.stream()
                        .noneMatch(requestRelation ->
                                requestRelation.getTargetEntityId().equals(serviceRelation.getTargetEntityId()) &&
                                        requestRelation.getEntityRelationTypeId().equals(serviceRelation.getEntityRelationTypeId())
                        )
                )
                .forEach(serviceRelation -> entityRelationRepository.deleteById(serviceRelation.getId()));
    }

}
