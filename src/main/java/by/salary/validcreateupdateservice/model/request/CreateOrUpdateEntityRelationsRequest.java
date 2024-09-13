package by.salary.validcreateupdateservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateEntityRelationsRequest {

    @NotNull
    private UUID sourceEntityId;

    @NotNull
    private UUID targetEntityId;

    private UUID dataSchemaConfigurationCode;

    @NotNull
    private DataDto data;

}
