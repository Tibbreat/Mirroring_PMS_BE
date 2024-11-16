package sep490.g13.pms_be.model.response.children;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenListByClassResponse {
    private String id;
    private String childName;
    private LocalDate childBirthDate;

    private String imageUrl;
    private String gender;

    private String fatherName;
    private String motherName;
}
