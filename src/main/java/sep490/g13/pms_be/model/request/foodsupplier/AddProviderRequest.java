package sep490.g13.pms_be.model.request.foodsupplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProviderRequest {
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

    private String createdBy;
}
