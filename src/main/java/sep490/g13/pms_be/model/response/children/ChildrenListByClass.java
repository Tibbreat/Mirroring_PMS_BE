package sep490.g13.pms_be.model.response.children;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenListByClass {
    private String id;
    private String avatar;
    private String childName;
    private LocalDate childBirth;
    private String gender;
    private boolean isRegisteredForBoarding;
    private boolean isRegisteredForTransport;
    private boolean isDisable;
}
