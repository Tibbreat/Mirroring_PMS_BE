package sep490.g13.pms_be.model.request.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSchoolRequest  {
    private String schoolName;
    private String phoneContact;
    private String emailContact;
    private String address;
    private String principalId;
}
