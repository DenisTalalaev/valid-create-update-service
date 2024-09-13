package by.salary.validcreateupdateservice.model.object;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServiceData {
    private Long entityDataId;
    private List<ServiceDataRelation> relations = new ArrayList<>();
}
