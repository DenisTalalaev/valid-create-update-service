package by.salary.validcreateupdateservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID sourceEntityId;

    private UUID targetEntityId;

    @ManyToOne
    private DataSchemaConfiguration dataSchemaConfiguration;

    private Timestamp createdDate;

    private Timestamp lastUpdated;

}
