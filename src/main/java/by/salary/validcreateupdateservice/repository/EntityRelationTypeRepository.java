package by.salary.validcreateupdateservice.repository;

import by.salary.validcreateupdateservice.entity.EntityRelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRelationTypeRepository extends JpaRepository<EntityRelationType, Integer> {
}
