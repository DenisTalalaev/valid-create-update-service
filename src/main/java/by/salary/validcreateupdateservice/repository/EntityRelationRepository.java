package by.salary.validcreateupdateservice.repository;

import by.salary.validcreateupdateservice.entity.EntityData;
import by.salary.validcreateupdateservice.entity.EntityRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityRelationRepository extends JpaRepository<EntityRelation, Long> {

    List<EntityRelation> findAllByEntityData(EntityData byId);
}
