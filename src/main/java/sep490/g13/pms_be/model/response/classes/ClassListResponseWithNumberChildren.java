package sep490.g13.pms_be.model.response.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sep490.g13.pms_be.utils.enums.ClassStatusEnums;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassListResponseWithNumberChildren {
    private String id;
    private String className;
    private String ageRange;
    private Date openingDay;
    private String managerId;
    private String managerName;
    private String academicYear;
    private int totalStudent;
    private int countStudentStudying;
    private ClassStatusEnums status;}
