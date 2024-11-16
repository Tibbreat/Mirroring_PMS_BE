package sep490.g13.pms_be.model.response.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListAvailableClassOption {
    private String id;
    private String className;
    private String ageRange;
    private String teacherName;
    private String teacherAccount;
    private int countStudent;
    private int maxStudent;
}
