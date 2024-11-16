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
public class AdmissionFile extends Auditable<String> {
    private String fileName;

    @ManyToOne
    @JsonBackReference
    private SchoolYearInformation schoolYearInformationId;

    private String note;
}
