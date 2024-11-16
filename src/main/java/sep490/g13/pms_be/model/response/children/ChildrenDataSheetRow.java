package sep490.g13.pms_be.model.response.children;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ChildrenDataSheetRow {
    private String childName;
    private LocalDate childBirthDate;
    private String gender;
    private String nationality;
    private String religion;
    private String birthAddress;
    private String childAddress;
    private String fatherName;
    private String motherName;
}
