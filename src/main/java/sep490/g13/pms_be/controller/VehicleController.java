package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.Vehicle;
import sep490.g13.pms_be.model.request.route.AddVehicleIntoRouteRequest;
import sep490.g13.pms_be.model.request.vehicle.AddVehicleRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.model.response.vehicle.VehicleAvailableResponse;
import sep490.g13.pms_be.service.entity.VehicleService;

import java.util.List;


@RestController
@RequestMapping("/pms/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<PagedResponseModel<Vehicle>> getAllVehicle(@PathVariable String providerId, @RequestParam int page) {
        Page<Vehicle> vehicles = vehicleService.getAllVehicle(providerId, page - 1, 10);
        List<Vehicle> results = vehicles.getContent();
        String msg = results.isEmpty() ? "No vehicle found" : "Get all vehicle successfully";
        return ResponseEntity.ok(PagedResponseModel.<Vehicle>builder()
                .page(page)
                .size(10)
                .msg(msg)
                .total(vehicles.getTotalElements())
                .listData(results)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<Vehicle> add(
            @RequestPart("request") AddVehicleRequest request,  // JSON part
            @RequestPart("images") List<MultipartFile> images  // File part (multiple files)
    ) {
        try {
            // Process the request object and images
            Vehicle addedVehicle = vehicleService.add(request, images);

            // Assuming your service method handles saving both the vehicle info and the image(s)
            return ResponseEntity.ok(addedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/update-status/{vehicleId}")
    public ResponseEntity<Void> updateStatus(@PathVariable String vehicleId) {
        vehicleService.updateStatus(vehicleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<VehicleAvailableResponse>> getVehicleAvailable() {
        return ResponseEntity.ok(vehicleService.getVehicleAvailable());
    }
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Vehicle>> getAllVehicleByRouteId(@PathVariable String routeId) {
        return ResponseEntity.ok(vehicleService.getAllVehicleByRouteId(routeId));
    }

    @PutMapping("/route-registered/{routeId}")
    public ResponseEntity<String> registerVehiclesToRoute(
            @PathVariable String routeId,
            @RequestBody List<AddVehicleIntoRouteRequest> vehicleIds) {
        vehicleService.registerVehicleToRoute(vehicleIds, routeId);
        return ResponseEntity.ok("Vehicles successfully registered to the route.");
    }

    @PutMapping("/unsubscribe-route/{vehicleId}")
    public ResponseEntity<String> unsubscribeRoute(@PathVariable String vehicleId) {
        vehicleService.unsubscribeRoute(vehicleId);
        return ResponseEntity.ok("Vehicle successfully unsubscribed from the route.");
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<Vehicle> findVehicleByManagerId(@PathVariable String managerId) {
        return ResponseEntity.ok(vehicleService.findVehicleByManagerId(managerId));
    }
}
