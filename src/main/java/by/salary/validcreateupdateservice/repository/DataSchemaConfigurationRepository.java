package by.salary.validcreateupdateservice.repository;

import by.salary.validcreateupdateservice.entity.DataSchemaConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataSchemaConfigurationRepository extends JpaRepository<DataSchemaConfiguration, Integer> {
    List<DataSchemaConfiguration> findAllByCode(UUID dataSchemaConfigurationCode);
}
