package by.salary.validcreateupdateservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataDto {

    @NotNull
    private List<RelationDto> relations;
}
