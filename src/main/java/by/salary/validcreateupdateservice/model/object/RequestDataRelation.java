package by.salary.validcreateupdateservice.model.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RequestDataRelation {

    private UUID targetEntityId;
    private Integer entityRelationTypeId;
}
