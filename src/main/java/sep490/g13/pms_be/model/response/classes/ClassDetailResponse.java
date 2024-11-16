package sep490.g13.pms_be.model.response.classes;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sep490.g13.pms_be.entities.Children;
import sep490.g13.pms_be.entities.ClassTeacher;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassDetailResponse {
    @NotNull(message = "Class Name không được để trống")
    private String className;
    @NotNull(message = "Tuổi không được để trống")
    private String ageRange;
    @NotNull(message = "Opening Day không được để trống")
    private Date openingDay;
    @NotNull(message = "Closing Day không được để trống")
    private Date closingDay;
    @NotNull(message = "Childrens không được để trống")
    private Set<String> children;
    @NotNull(message = "Teachers không được để trống")
    private Set<String> teachers;

}
