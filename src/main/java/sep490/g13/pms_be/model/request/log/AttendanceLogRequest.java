package sep490.g13.pms_be.model.request.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceLogRequest {
    private String childrenId;
    private String userId;
}
