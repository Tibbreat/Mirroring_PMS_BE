package sep490.g13.pms_be.model.request.children;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AddParentRequest {
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;
    @NotNull(message = "Số căn cước không được để trống")

    private String idCardNumber;
    @NotNull(message = "Số điện thoại không được để trống")
    private String phone;
}
