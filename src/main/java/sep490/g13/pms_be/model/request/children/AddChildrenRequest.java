package sep490.g13.pms_be.model.request.children;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddChildrenRequest {
    @NotEmpty(message = "Tên trẻ không được để trống")
    private String childName;
    @NotEmpty(message = "Ngày sinh không được để trống")
    private LocalDate childBirthDate;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String childAddress;
    @NotEmpty(message = "Địa chỉ khai sinh không được để trống")
    private String birthAddress;
    @NotEmpty(message = "Quốc tịch không được để trống")
    private String nationality;
    @NotEmpty(message = "Dân tộc không được để trống")
    private String religion;
    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;

    private AddParentRequest father;

    private AddParentRequest mother;

    private Boolean isDisabled;

    private String note;

    private String createdBy;

    private String classId;

    private boolean isDuplicate;
}
