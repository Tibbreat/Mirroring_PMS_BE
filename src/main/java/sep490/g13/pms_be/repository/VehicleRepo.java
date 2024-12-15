package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Vehicle;
import sep490.g13.pms_be.model.response.vehicle.VehicleAvailableResponse;

import java.util.List;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, String> {

    @Query("SELECT v FROM Vehicle v WHERE v.transport.id = :transportId")
    Page<Vehicle> findAllByTransportId(String transportId, Pageable pageable);

    @Query("SELECT COUNT(v.id) FROM Vehicle v WHERE v.transport.id = :transportId")
    int countByTransport_Id(String transportId);

    @Modifying
    @Query("UPDATE Vehicle v SET v.isActive = :status WHERE v.id = :vehicleId")
    void updateStatus(String vehicleId, Boolean status);

    @Query("SELECT new sep490.g13.pms_be.model.response.vehicle.VehicleAvailableResponse(" +
            "v.id, v.vehicleName, " +
            "v.manufacturer, v.numberOfSeats, " +
            "v.color, v.licensePlate, " +
            "v.transport.providerName) " +
            "FROM Vehicle v WHERE v.route IS NULL AND v.isActive = true")
    List<VehicleAvailableResponse> getVehicleAvailable();

    @Query("SELECT v FROM Vehicle v WHERE v.route.id = :routeId")
    List<Vehicle> findAllByRouteId(String routeId);

    @Query("SELECT v.id FROM Vehicle v WHERE v.transport.id = :transportId")
    List<String> findAllByTransportId(String transportId);

    @Query("SELECT v.id FROM Vehicle v WHERE v.route.id = :routeId")
    List<String> findAllByRoute(String routeId);

    @Modifying
    @Query("UPDATE Vehicle v SET v.numberChildrenRegistered = :numberChildrenRegistered WHERE v.id = :vehicleId")
    void updateNumberChildrenRegistered(String vehicleId, int numberChildrenRegistered);

    @Modifying
    @Query("UPDATE Vehicle v SET v.route = null , v.numberChildrenRegistered = 0, v.manager = null WHERE v.id = :vehicleId")
    void removeRoute(String vehicleId);


    @Query("SELECT v FROM Vehicle v WHERE v.manager.id = :managerId")
    Vehicle findVehicleByManagerId(String managerId);

    List<Vehicle> findAllByRouteIdOrderByNumberOfSeatsDesc(String routeId);

    @Query("SELECT (COUNT(v.id) > 0) FROM Vehicle v WHERE v.licensePlate = :licensePlate")
    boolean existsByLicensePlate(String licensePlate);
}
