package sep490.g13.pms_be.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteSubmittedApplication extends Auditable<String> {

    @ManyToOne
    private Route route;

    private String routeName;

    @ManyToOne
    private Children children;

    private String childrenName;

    @ManyToOne
    private StopLocation stopLocation;

    private String stopLocationName;

    private String academicYear;
    private String status;
}
