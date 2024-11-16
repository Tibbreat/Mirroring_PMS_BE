package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenRoute  extends Auditable<String> {
    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    @ManyToOne
    @JoinColumn(name = "children_id")
    @JsonBackReference
    private Children children;
}
