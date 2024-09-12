package by.salary.validcreateupdateservice.entity;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private DataSchemaConfiguration dataSchemaConfiguration;
    @ManyToOne
    private CtDataSchemaType ctDataSchemaType;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonSchema jsonSchema;
}
