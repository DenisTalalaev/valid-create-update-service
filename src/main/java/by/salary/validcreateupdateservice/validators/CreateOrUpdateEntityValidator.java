package by.salary.validcreateupdateservice.validators;

import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.repository.CtDataSchemaTypeRepository;
import by.salary.validcreateupdateservice.repository.DataSchemaConfigurationRepository;
import by.salary.validcreateupdateservice.repository.DataSchemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import lombok.RequiredArgsConstructor;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateEntityValidator implements AbstractValidator {

    private final DataSchemaConfigurationRepository dataSchemaConfigurationRepository;
    private final DataSchemaRepository dataSchemaRepository;
    private final CtDataSchemaTypeRepository ctDataSchemaTypeRepository;

    private final String dataSchemaTypeName = "validateInstance";

    @Override
    public boolean validate(Object object) throws CreateOrUpdateEntityRelationsException {
        CreateOrUpdateEntityRelationsRequest request = (CreateOrUpdateEntityRelationsRequest) object;
        DataSchemaConfiguration dataSchemaConfiguration = validateConfiguration(request);
        CtDataSchemaType ctDataSchemaType = findCtDataSchemaType(dataSchemaTypeName);
        DataSchema dataSchema = findDataSchema(dataSchemaConfiguration, ctDataSchemaType);

        return validateDataSchema(dataSchema.getJsonSchema(), request);
    }

    private boolean validateDataSchema(JsonSchema jsonSchema, CreateOrUpdateEntityRelationsRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(request);

            JSONObject jsonObject = new JSONObject(jsonString);

            Schema schema = SchemaLoader.load(new JSONObject(jsonSchema.toString()));
            schema.validate(jsonObject);

        } catch (ValidationException e) {
            throw new CreateOrUpdateEntityRelationsException(
                    "Data schema validation failed: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new CreateOrUpdateEntityRelationsException(
                    "Data schema validation failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return true;
    }

    private DataSchema findDataSchema(DataSchemaConfiguration dataSchemaConfiguration, CtDataSchemaType ctDataSchemaType) {
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

    private CtDataSchemaType findCtDataSchemaType(String dataSchemaTypeName) {
        return ctDataSchemaTypeRepository.findByName(dataSchemaTypeName).orElseThrow(
                () -> new CreateOrUpdateEntityRelationsException(
                        "Data schema type with name: " + dataSchemaTypeName + " not found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    //
    private DataSchemaConfiguration validateConfiguration(CreateOrUpdateEntityRelationsRequest request) throws CreateOrUpdateEntityRelationsException {
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
