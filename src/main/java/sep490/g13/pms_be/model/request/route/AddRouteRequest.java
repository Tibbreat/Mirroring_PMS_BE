package sep490.g13.pms_be.model.request.route;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRouteRequest {
    private String routeName;
    private String startLocation;
    private String endLocation;
    private String pickupTime;
    private String dropOffTime;
    private String createdBy;

    private List<String> stopLocations;
}
