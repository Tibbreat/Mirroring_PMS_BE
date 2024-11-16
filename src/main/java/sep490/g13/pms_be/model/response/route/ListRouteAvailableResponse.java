package sep490.g13.pms_be.model.response.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListRouteAvailableResponse {
    private String id;
    private String routeName;
    private String startLocation;
    private String endLocation;
    private String pickupTime;
    private String dropOffTime;
    private Boolean isActive;

    private List<String> stopLocation;


}
