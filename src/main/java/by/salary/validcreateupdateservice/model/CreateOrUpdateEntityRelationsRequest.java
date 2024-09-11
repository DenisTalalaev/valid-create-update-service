package by.salary.validcreateupdateservice.model;

import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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
