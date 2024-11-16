package sep490.g13.pms_be.model.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "email không được để trống")
    private String email;
    @NotNull(message = "Mật khẩu hiện tại không được để trống")
    private String oldPassword;

    @NotNull(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotNull(message = "Mật khẩu xác nhận không được để trống")
    private String confirmPassword;
}
