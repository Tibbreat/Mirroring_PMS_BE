package sep490.g13.pms_be.model.request.classes;

import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddClassRequest {
    @NotNull(message = "Class Name không được để trống")
    private String className;

    @NotNull(message = "Tuổi không được để trống")
    private String ageRange;

    @NotNull(message = "Opening Day không được để trống")
    private Date openingDay;

    @NotNull(message = "Danh sách giáo viên không được để trống")
    private String teacherId;

    @NotNull(message = "Quản lý lớp không được để trống")
    private String managerId;

    @NotNull(message = "Created By Không được để trống")
    private String createdBy;

    @NotNull(message = "Academic Year không được để trống")
    private String academicYear;
}
