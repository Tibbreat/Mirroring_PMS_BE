package sep490.g13.pms_be.model.request.classes;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateClassRequest {
    @NotNull(message = "Opening Day không được để trống")
    private Date openingDay;
    @NotNull(message = "Closing Day không được để trống")
    private Date closingDay;
    @NotNull(message = "Quản lý lớp không được để trống")
    private String managerId;
    private List<String> teacherId;
    @NotNull(message = "Last Modify By Không được để trống")
    private String lastModifyById;
}
