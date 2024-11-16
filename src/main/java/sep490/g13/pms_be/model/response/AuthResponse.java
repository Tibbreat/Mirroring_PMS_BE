package sep490.g13.pms_be.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String tokenType = "Bearer";
    private String token;
    private String role;
    private String message;
    private String uid;
    private String username;
    private String fullName;
}
