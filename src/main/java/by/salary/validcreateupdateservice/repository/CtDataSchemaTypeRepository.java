package by.salary.validcreateupdateservice.repository;

import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import by.salary.validcreateupdateservice.entity.EntityRelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CtDataSchemaTypeRepository extends JpaRepository<CtDataSchemaType, Integer> {
    Optional<CtDataSchemaType> findByName(String code);

    Optional<EntityRelationType> findByCode(UUID entityRelationTypeCode);
}
