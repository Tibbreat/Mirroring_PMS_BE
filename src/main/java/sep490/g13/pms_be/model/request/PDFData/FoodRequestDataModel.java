package sep490.g13.pms_be.model.request.PDFData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FoodRequestDataModel {
    private String schoolId;
    private String providerId;
}
