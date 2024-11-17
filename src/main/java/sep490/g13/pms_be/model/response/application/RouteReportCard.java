package sep490.g13.pms_be.model.response.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteReportCard {
    private String routeId;
    private String routeName;
    private Long totalRegister;
    private Long countRegistered;
    private Long countApproved;
}
