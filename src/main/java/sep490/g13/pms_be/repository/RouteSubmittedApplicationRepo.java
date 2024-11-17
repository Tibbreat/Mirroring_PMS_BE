package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.RouteSubmittedApplication;
import sep490.g13.pms_be.model.response.application.RouteApplication;
import sep490.g13.pms_be.model.response.application.RouteReportCard;

import java.util.List;

@Repository
public interface RouteSubmittedApplicationRepo extends JpaRepository<RouteSubmittedApplication, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.application.RouteApplication(" +
            "c.id, c.children.id, c.childrenName, c.route.id, c.routeName, c.stopLocation.id, c.stopLocationName, " +
            "c.children.vehicle.vehicleName, c.status) " +
            "FROM RouteSubmittedApplication c " +
            "LEFT JOIN c.children.vehicle " +
            "WHERE c.academicYear = :academicYear " +
            "AND (:routeId IS NULL OR c.route.id = :routeId) " +
            "ORDER BY c.routeName")
    List<RouteApplication> findAllByAcademicYear(@Param("academicYear") String academicYear,
                                                 @Param("routeId") String routeId);

    @Query("SELECT new sep490.g13.pms_be.model.response.application.RouteReportCard(" +
            "r.id, r.routeName, " +
            "COUNT(rs.id), " + // Tổng số đăng ký
            "SUM(CASE WHEN rs.status = 'PENDING' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN rs.status = 'APPROVED' THEN 1 ELSE 0 END)) " +
            "FROM Route r " +
            "LEFT JOIN RouteSubmittedApplication rs ON rs.route.id = r.id AND rs.academicYear = :academicYear " +
            "GROUP BY r.id, r.routeName")
    List<RouteReportCard> findAllRouteReport(@Param("academicYear") String academicYear);

    @Modifying
    @Query("UPDATE RouteSubmittedApplication c " +
            "SET c.status = :status " +
            "WHERE c.id = :id")
    void updateStatusById(String id, String status);


}
