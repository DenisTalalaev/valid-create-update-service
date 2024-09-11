package by.salary.validcreateupdateservice.accessors;

import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.repository.CtDataSchemaTypeRepository;
import by.salary.validcreateupdateservice.repository.DataSchemaConfigurationRepository;
import by.salary.validcreateupdateservice.repository.DataSchemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateAccessor {
    private final DataSchemaConfigurationRepository dataSchemaConfigurationRepository;
    private final DataSchemaRepository dataSchemaRepository;
    private final CtDataSchemaTypeRepository ctDataSchemaTypeRepository;



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
    public DataSchemaConfiguration validateConfiguration(CreateOrUpdateEntityRelationsRequest request) throws CreateOrUpdateEntityRelationsException {
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

}
