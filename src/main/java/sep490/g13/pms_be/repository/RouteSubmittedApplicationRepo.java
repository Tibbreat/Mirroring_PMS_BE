package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.RouteSubmittedApplication;
import sep490.g13.pms_be.model.response.application.RouteApplication;

import java.util.List;

@Repository
public interface RouteSubmittedApplicationRepo extends JpaRepository<RouteSubmittedApplication, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.application.RouteApplication(" +
            "c.children.id, c.childrenName, c.route.id, c.routeName, c.stopLocation.id, c.stopLocationName, c.status) " +
            "FROM RouteSubmittedApplication c " +
            "WHERE c.academicYear = :academicYear " +
            "AND (:routeId IS NULL OR c.route.id = :routeId) " +
            "ORDER BY c.routeName")
    List<RouteApplication> findAllByAcademicYear(@Param("academicYear") String academicYear,
                                                 @Param("routeId") String routeId);

}
