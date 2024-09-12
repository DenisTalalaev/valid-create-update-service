package by.salary.validcreateupdateservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private EntityData entityData;

    private UUID targetEntityId;

    @ManyToOne
    private EntityRelationType entityRelationType;
}
