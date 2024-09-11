package by.salary.validcreateupdateservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import java.util.UUID;

@Getter
@Setter
public class RelationDto {

    @NotEmpty
    @Size(min = 1)
    private List<UUID> targetEntiyIdList;

    private UUID entityRelationTypeCode;

}
