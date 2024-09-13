package by.salary.validcreateupdateservice.accessors;

import by.salary.validcreateupdateservice.entity.*;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.object.CreateOrUpdateEntity;
import by.salary.validcreateupdateservice.model.request.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.object.RequestDataRelation;
import by.salary.validcreateupdateservice.model.object.ServiceDataRelation;
import by.salary.validcreateupdateservice.repository.entities.EntityDataRepository;
import by.salary.validcreateupdateservice.repository.entities.EntityRelationRepository;
import by.salary.validcreateupdateservice.repository.entities.EntityRelationTypeRepository;
import by.salary.validcreateupdateservice.repository.schemas.CtDataSchemaTypeRepository;
import by.salary.validcreateupdateservice.repository.schemas.DataSchemaConfigurationRepository;
import by.salary.validcreateupdateservice.repository.schemas.DataSchemaRepository;
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
        return entityRelationTypeRepository.findByCode(entityRelationTypeCode).orElseThrow(
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
        return entityRelationRepository.findAllByEntityData(entityDataRepository.getById(entityDataId))
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

        for (RequestDataRelation requestDataRelation : createOrUpdateEntity.getRequestData().getRelations()) {
            entityRelationRepository.save(
                    EntityRelation.builder()
                            .entityData(entityData)
                            .targetEntityId(requestDataRelation.getTargetEntityId())
                            .entityRelationType(
                                    entityRelationTypeRepository.getById(
                                            requestDataRelation.getEntityRelationTypeId()
                                    )
                            )
                            .build()
            );
        }

    }

    public void updateDataEntity(CreateOrUpdateEntity createOrUpdateEntity) {
        EntityData entityData = entityDataRepository.getById(createOrUpdateEntity.getServiceData().getEntityDataId());

        entityData.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        entityData.setDataSchemaConfiguration(dataSchemaConfigurationRepository.getById(createOrUpdateEntity.getRequestData().getDataSchemaConfigurationId()));

        entityDataRepository.save(entityData);

        List<RequestDataRelation> requestRelations = createOrUpdateEntity.getRequestData().getRelations();
        List<ServiceDataRelation> serviceRelations = createOrUpdateEntity.getServiceData().getRelations();

        for (RequestDataRelation requestRelation : requestRelations) {
            boolean found = false;
            for (ServiceDataRelation serviceRelation : serviceRelations) {
                if (requestRelation.getTargetEntityId().equals(serviceRelation.getTargetEntityId()) &&
                        requestRelation.getEntityRelationTypeId().equals(serviceRelation.getEntityRelationTypeId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                EntityRelation newRelation = EntityRelation.builder()
                        .entityData(entityData)
                        .targetEntityId(requestRelation.getTargetEntityId())
                        .entityRelationType(entityRelationTypeRepository.getById(requestRelation.getEntityRelationTypeId()))
                        .build();
                entityRelationRepository.save(newRelation);
            }
        }

        for (ServiceDataRelation serviceRelation : serviceRelations) {
            boolean found = false;
            for (RequestDataRelation requestRelation : requestRelations) {
                if (requestRelation.getTargetEntityId().equals(serviceRelation.getTargetEntityId()) &&
                        requestRelation.getEntityRelationTypeId().equals(serviceRelation.getEntityRelationTypeId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                entityRelationRepository.deleteById(serviceRelation.getId());
            }
        }
    }

}
