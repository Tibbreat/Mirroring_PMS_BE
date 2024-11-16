package sep490.g13.pms_be.model.request.children;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String childName;
    @NotNull
    private LocalDate childBirthDate;
    @NotNull
    private String childAddress;
    @NotNull
    private String birthAddress;
    @NotNull
    private String nationality;
    @NotNull
    private String religion;
    @NotNull
    private String gender;

    private AddParentRequest father;

    private AddParentRequest mother;

    private Boolean isDisabled;

    private String note;

    private String createdBy;

    private String classId;
}
