package by.salary.validcreateupdateservice.repository;

import by.salary.validcreateupdateservice.entity.EntityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntityDataRepository extends JpaRepository<EntityData, Integer> {

    Optional<EntityData> findByTargetEntityIdAndSourceEntityId(UUID targetEntityId, UUID sourceEntityId);
}
