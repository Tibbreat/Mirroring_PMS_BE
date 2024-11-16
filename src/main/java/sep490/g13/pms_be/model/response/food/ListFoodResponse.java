package sep490.g13.pms_be.model.response.food;

import lombok.*;
import sep490.g13.pms_be.entities.FoodRequestItem;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListFoodResponse {
    private String id;
    private String dayNeeded;
    private String requestOwner;
    private Date requestDate;
    private String status;
}
