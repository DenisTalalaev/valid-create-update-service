package by.salary.validcreateupdateservice.repository.schemas;

import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.DataSchema;
import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataSchemaRepository extends JpaRepository<DataSchema, Integer> {

    Optional<DataSchema> findByDataSchemaConfigurationAndCtDataSchemaType(
            DataSchemaConfiguration dataSchemaConfiguration,
            CtDataSchemaType ctDataSchemaType
    );
}
