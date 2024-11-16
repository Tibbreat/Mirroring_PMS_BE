package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopLocation extends Auditable<String> {

    private String locationName;

    private int stopOrder;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonBackReference
    private Route route;
}
