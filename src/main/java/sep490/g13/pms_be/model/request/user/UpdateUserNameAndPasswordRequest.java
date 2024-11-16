package sep490.g13.pms_be.model.request.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserNameAndPasswordRequest {
    private String userName;
    private String password;
    private String email;
    private String fullName;

    public UpdateUserNameAndPasswordRequest(String fullName) {
        this.fullName = fullName;
    }
}
