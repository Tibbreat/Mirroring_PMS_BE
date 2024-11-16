package sep490.g13.pms_be.model.response.attendance;

import lombok.*;
import sep490.g13.pms_be.utils.enums.AttendanceStatusEnums;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenAttendanceLog {
    private String id;
    private String logId;
    private String imageUrl;
    private String childName;
    private Date morningBoardingTime;
    private Date morningAlightingTime;
    private Date afternoonBoardingTime;
    private Date afternoonAlightingTime;
    private AttendanceStatusEnums status;
    private String note;
}
