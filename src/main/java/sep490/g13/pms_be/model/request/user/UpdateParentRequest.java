package sep490.g13.pms_be.model.request.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateParentRequest {
    private String childrenId;
    private ParentDetails father;
    private ParentDetails mother;

    @Setter
    @Getter
    public static class ParentDetails {
        private String fullName;
        private String phone;
    }
}
