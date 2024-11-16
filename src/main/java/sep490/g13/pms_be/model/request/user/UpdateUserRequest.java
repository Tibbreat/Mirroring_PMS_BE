package sep490.g13.pms_be.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sep490.g13.pms_be.utils.enums.RoleEnums;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String fullName;
    private String address;
    private String phone;
    private String contractType;
    private RoleEnums role;
    private String idCardNumber;
}
