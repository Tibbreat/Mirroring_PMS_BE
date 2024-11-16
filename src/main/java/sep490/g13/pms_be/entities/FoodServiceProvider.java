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
public class FoodServiceProvider extends Auditable<String> {
    private String providerName;
    private String providerAddress;
    private String providerTaxCode;
    private String providerPhone;
    private String providerEmail;

    //Người đại diện
    private String representativeName;
    private String representativePosition;

    //Thông tin ngân hàng
    private String bankName;
    private String bankAccountNumber;
    private String beneficiaryName;

    private Boolean isActive;

    @ManyToOne
    private School school;
}
