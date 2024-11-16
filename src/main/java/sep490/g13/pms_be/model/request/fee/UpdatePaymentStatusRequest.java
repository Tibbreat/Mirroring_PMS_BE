package sep490.g13.pms_be.model.request.fee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentStatusRequest {
    private String childrenFeeId;
    private Boolean isPayed;
}