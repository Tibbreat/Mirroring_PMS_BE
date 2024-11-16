package sep490.g13.pms_be.model.response.children;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenDataSheetByVehicle {
    private String childId;
    private String childName;
    private String childrenClassName;
    private String childrenImage;
    private String vehicleName;
    private String stopLocationRegistered;
    private String childrenGender;
}
