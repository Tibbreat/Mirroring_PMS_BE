package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleImage extends Auditable<String> {

    private String imageUrl;
    private String imageType;

    @ManyToOne
    @JsonBackReference
    private Vehicle vehicle;
}
