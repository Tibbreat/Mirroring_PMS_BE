package sep490.g13.pms_be.model.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
