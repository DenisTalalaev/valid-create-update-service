package by.salary.validcreateupdateservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityRelationType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private UUID code;

    @Size(max = 255)
    @NotNull
    private String name;

    @Size(max = 255)
    @NotNull
    private String description;
}
