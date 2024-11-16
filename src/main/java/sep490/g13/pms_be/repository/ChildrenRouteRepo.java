package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.ChildrenRoute;

import java.util.List;

@Repository
public interface ChildrenRouteRepo extends JpaRepository<ChildrenRoute, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ChildrenRoute cr WHERE cr.children.id = :childrenId")
    void deleteByChildrenId(@Param("childrenId") String childrenId);

    @Query("SELECT cr.children.id FROM ChildrenRoute cr WHERE cr.route.id = :routeId")
    List<String> findAllChildrenByRouteId(String routeId);


    @Query("SELECT COUNT(cr.children.id) FROM ChildrenRoute cr " +
            "JOIN ChildrenClass cc ON cr.children.id = cc.children.id AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING " +
            "WHERE cr.route.id = :routeId AND cc.classes.id = :classId")
    int countChildrenOfClassRegisteredThisRoute(@Param("routeId") String routeId,
                                                @Param("classId") String classId);
}
