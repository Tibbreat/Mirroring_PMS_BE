package sep490.g13.pms_be.model.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataResponse {
    private String id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String imageLink;
    private Boolean isActive;
    private String schoolId;
    private String role;
}
