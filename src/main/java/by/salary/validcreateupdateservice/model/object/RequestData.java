package by.salary.validcreateupdateservice.model.object;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

import java.util.List;

@Getter
@Setter
public class RequestData {
    private UUID sourceEntityId;
    private UUID targetEntityId;
    private Integer dataSchemaConfigurationId;
    private List<RequestDataRelation> relations = new ArrayList<>();
}
