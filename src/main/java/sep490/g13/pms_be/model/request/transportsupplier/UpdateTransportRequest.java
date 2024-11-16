package sep490.g13.pms_be.model.request.transportsupplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateTransportRequest {
    @NotBlank(message = "Tên người đại diện không được bỏ trống")
    private String representativeName;

    @NotBlank(message = "Số điện thoại nhà cung cấp k")
    private String providerPhone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải đúng định dạng")
    private String providerEmail;

    @NotBlank(message = "Địa chỉ nhà cung cấp không được để trống")
    private String providerAddress;

    @NotNull(message = "Last Modify By không được để trống")
    private String lastModifyById;

    private Date lastModifyDate;
}
