package sep490.g13.pms_be.model.request.school;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSchoolRequest {
    @NotEmpty(message = "Tên trường không được để trống")
    private String schoolName;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phoneContact;

    @NotEmpty(message = "Email không được để trống")
    private String emailContact;

    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;

    @NotEmpty(message = "Tên hiệu trưởng không được để trống")
    private String principalName;

    @NotEmpty(message = "Số điện thoại hiệu trưởng không được để trống")
    private String principalPhone;
}
