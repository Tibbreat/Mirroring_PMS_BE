package sep490.g13.pms_be.model.response.teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherOptionResponse {
    private String id;
    private String className;
    private String fullName;
}
