package sep490.g13.pms_be.model.request.fee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFeeToChildrenRequest {
    private String childId;
    private String feeId;
    private String amount;
    private LocalDate dueDate;
}