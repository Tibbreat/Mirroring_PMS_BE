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
public class TransportServiceProvider extends Auditable<String> {
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

    //Số lượng phương tiện
    private Integer totalVehicle;

    private Boolean isActive;

    @ManyToOne
    private School school;
}

