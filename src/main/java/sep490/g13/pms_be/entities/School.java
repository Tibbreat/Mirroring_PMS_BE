package sep490.g13.pms_be.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School extends Auditable<String> {
    private String schoolName;
    private String phoneContact;
    private String emailContact;
    private String schoolAddress;
    private String schoolAddressCity;

    @OneToOne
    private User principal;
}
