package sep490.g13.pms_be.model.response.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sep490.g13.pms_be.utils.enums.ClassStatusEnums;
import sep490.g13.pms_be.utils.enums.StudyStatusEnums;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListClassWithStudyStatus {
    private String id;
    private String academicYear;
    private String className;

    private ClassStatusEnums classStatus;
    private StudyStatusEnums studyStatus;
}
