package by.salary.validcreateupdateservice.validators;

import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.exceptions.CreateOrUpdateEntityRelationsException;
import by.salary.validcreateupdateservice.model.request.CreateOrUpdateEntityRelationsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import lombok.RequiredArgsConstructor;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateEntityValidator implements AbstractValidator<CreateOrUpdateEntityRelationsRequest, DataSchema> {



    @Override
    public boolean validate(CreateOrUpdateEntityRelationsRequest request,
                            DataSchema dataSchema) {
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

}
