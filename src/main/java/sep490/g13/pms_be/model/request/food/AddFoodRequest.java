package sep490.g13.pms_be.model.request.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sep490.g13.pms_be.entities.FoodRequestItem;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFoodRequest {
    private String dayNeeded;
    private String providerId;
    private String createdBy;
    private List<FoodRequestItem> foodRequestItems;

}
