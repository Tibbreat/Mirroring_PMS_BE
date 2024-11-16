package sep490.g13.pms_be.model.request.transportsupplier;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTransportProviderRequest {
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

    private String createdBy;


}
