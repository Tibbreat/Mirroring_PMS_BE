package sep490.g13.pms_be.model.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sep490.g13.pms_be.utils.Constant;

@Data
public class ChangePasswordRequest {
    @NotNull(message = Constant.NOT_BLANK_EMAIL_MESSAGE)
    private String email;

    @NotNull(message = Constant.NOT_BLANK_PASSWORD_MESSAGE)
    private String newPassword;

    @NotNull(message = Constant.NOT_BLANK_CONFIRM_PASSWORD_MESSAGE)
    private String reNewPassword;
}
