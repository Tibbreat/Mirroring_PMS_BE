package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route extends Auditable<String> {

    private String routeName;
    private String startLocation;
    private String endLocation;
    private String pickupTime;
    private String dropOffTime;
    private Boolean isActive;

    @OneToMany(mappedBy = "route")
    @JsonManagedReference
    @JsonIgnore
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "route")
    @JsonManagedReference
    @OrderBy("stopOrder ASC")
    private Set<StopLocation> stopLocations;

    @OneToMany(mappedBy = "route")
    @JsonManagedReference
    @JsonIgnore
    private Set<ChildrenRoute> childrenRoutes;
}
