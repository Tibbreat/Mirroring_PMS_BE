package sep490.g13.pms_be.model.response.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteApplication {
    private String applicationId;
    private String childrenId;
    private String childrenName;
    private String routeId;
    private String routeName;
    private String stopLocationId;
    private String stopLocationName;
    private String vehicleName;
    private String status;
}
