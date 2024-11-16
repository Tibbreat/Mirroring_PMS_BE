package sep490.g13.pms_be.model.response.classes;

import lombok.*;
import sep490.g13.pms_be.utils.enums.ClassStatusEnums;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassListResponse {
    private String id;
    private String className;
    private String ageRange;
    private Date openingDay;
    private String managerId;
    private String managerName;
    private String academicYear;
    private int totalStudent;
    private ClassStatusEnums status;
}
