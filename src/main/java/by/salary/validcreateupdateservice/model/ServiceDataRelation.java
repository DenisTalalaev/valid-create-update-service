package by.salary.validcreateupdateservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ServiceDataRelation {
    private Long id;
    private UUID targetEntityId;
    private Integer entityRelationTypeId;
}
