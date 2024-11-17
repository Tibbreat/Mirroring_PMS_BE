package sep490.g13.pms_be.model.request.admission_application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovedTransportApplication {
    private String applicationId;
    private String routeId;
    private String stopLocationId;
    private String childrenId;
}
