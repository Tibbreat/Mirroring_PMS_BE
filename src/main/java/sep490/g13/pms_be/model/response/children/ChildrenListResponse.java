package sep490.g13.pms_be.model.response.children;

import lombok.*;
import sep490.g13.pms_be.model.request.RelationshipRequest;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenListResponse {
    private String id;
    private String childName;
    private LocalDate childBirthDate;
    private String classId;
    private String className;

    private String imageUrl;
    private String gender;

    private String fatherName;
    private String motherName;
}
