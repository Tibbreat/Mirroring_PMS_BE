package sep490.g13.pms_be.model.response.children;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenOptionResponse {
    private String id;
    private String fullName;
    private String className;
    private String parentId;
    private String parentName;
}
