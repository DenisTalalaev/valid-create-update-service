package by.salary.validcreateupdateservice.repository.schemas;

import by.salary.validcreateupdateservice.entity.CtDataSchemaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CtDataSchemaTypeRepository extends JpaRepository<CtDataSchemaType, Integer> {
    Optional<CtDataSchemaType> findByName(String code);
}
