package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Route;
import sep490.g13.pms_be.model.response.route.ListRouteResponse;

import java.util.List;

@Repository
public interface RouteRepo extends JpaRepository<Route, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.route.ListRouteResponse(" +
            "r.id, r.routeName, r.startLocation, r.endLocation, r.pickupTime, " +
            "r.dropOffTime, r.isActive, COUNT(cr)) " +
            "FROM Route r LEFT JOIN r.childrenRoutes cr " +
            "GROUP BY r.id, r.routeName, r.startLocation, r.endLocation, r.pickupTime, r.dropOffTime, r.isActive " +
            "ORDER BY r.routeName")
    Page<ListRouteResponse> getAllRoute(Pageable pageable);

    @Query("SELECT new sep490.g13.pms_be.model.response.route.ListRouteResponse(" +
            "r.id, r.routeName, r.startLocation, r.endLocation, r.pickupTime, " +
            "r.dropOffTime, r.isActive, COUNT(cr)) " +
            "FROM Route r LEFT JOIN r.childrenRoutes cr " +
            "WHERE r.id = :routeId " +
            "GROUP BY r.id, r.routeName, r.startLocation, r.endLocation, r.pickupTime, r.dropOffTime, r.isActive")
    ListRouteResponse getRoute(@Param("routeId") String routeId);

    @Query("SELECT  r FROM Route r WHERE r.isActive = true" +
            " ORDER BY r.routeName")
    List<Route> getAvailableRoute();

    @Modifying
    @Query("UPDATE Route r SET r.isActive = :status WHERE r.id = :routeId")
    void changeStatus(String routeId, Boolean status);


    @Modifying
    @Query("DELETE FROM ChildrenRoute cr WHERE cr.route.id = :routeId")
    void deleteChildrenRouteByRouteId(@Param("routeId") String routeId);

    @Modifying
    @Query("DELETE FROM ChildrenRoute cr WHERE cr.children.id = :childrenId")
    void deleteChildrenRouteByChildrenId(@Param("childrenId") String childrenId);
}
