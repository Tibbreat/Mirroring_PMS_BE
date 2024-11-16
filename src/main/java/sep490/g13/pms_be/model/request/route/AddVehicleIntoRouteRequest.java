package sep490.g13.pms_be.model.request.route;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddVehicleIntoRouteRequest {
    private String vehicleId;
    private String managerId;
}
