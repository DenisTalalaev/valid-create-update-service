package by.salary.validcreateupdateservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataSchemaConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private UUID code;

    private Timestamp validityDateFrom;

    private Timestamp validityDateTo;

    private Boolean isActive;

    @Size(max = 255)
    private String description;
}
