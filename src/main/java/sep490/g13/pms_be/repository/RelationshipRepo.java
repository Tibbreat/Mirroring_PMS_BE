package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Children;
import sep490.g13.pms_be.entities.Relationship;

import java.util.List;

@Repository
public interface RelationshipRepo extends JpaRepository<Relationship, String> {
    List<Relationship> findByChildrenId(Children children);
}
