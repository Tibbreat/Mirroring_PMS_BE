package sep490.g13.pms_be.model.request.fee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddFeeRequest {
    private String feeTitle;
    private Boolean isActive;
    private String createdById;
}
