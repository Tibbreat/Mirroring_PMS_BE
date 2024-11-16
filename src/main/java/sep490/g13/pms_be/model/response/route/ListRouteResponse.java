package sep490.g13.pms_be.model.response.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListRouteResponse {
    private String id;
    private String routeName;
    private String startLocation;
    private String endLocation;
    private String pickupTime;
    private String dropOffTime;
    private Boolean isActive;
    private Long childrenCount;
}
