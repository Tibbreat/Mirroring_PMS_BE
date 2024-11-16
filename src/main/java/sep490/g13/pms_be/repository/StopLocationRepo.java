package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.StopLocation;

import java.util.List;

@Repository
public interface StopLocationRepo extends JpaRepository<StopLocation, String> {

    @Query("SELECT sl FROM StopLocation sl WHERE sl.route.id = :routeId ORDER BY sl.stopOrder")
    List<StopLocation> getStopLocationByRouteId(String routeId);
}
