package sep490.g13.pms_be.model.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import sep490.g13.pms_be.utils.enums.RoleEnums;

import java.sql.Date;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AddUserRequest {

    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Số căn cước không được để trống")
    @Pattern(regexp = "\\d{12}", message = "Số căn cước phải là chuỗi gồm 12 chữ số")
    private String idCardNumber;

    @NotNull(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại phải là chuỗi gồm 10 chữ số")
    private String phone;

    @NotNull(message = "Role không được để trống")
    private RoleEnums role;

    private Date dob;

    private String contractType;
}
