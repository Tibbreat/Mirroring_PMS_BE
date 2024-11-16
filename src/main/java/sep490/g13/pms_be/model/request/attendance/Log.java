package sep490.g13.pms_be.model.request.attendance;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Log {
    private String attendanceDate;
    private String classId;
    private List<ChildAttendance> children;

    @Getter
    @Setter
    public static class ChildAttendance {
        private String childrenId;
        private String status;
        private String note;
    }
}
