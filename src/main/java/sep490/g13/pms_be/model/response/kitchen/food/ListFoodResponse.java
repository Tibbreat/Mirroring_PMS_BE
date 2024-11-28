package sep490.g13.pms_be.model.response.kitchen.food;

import lombok.*;

import java.util.Date;

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
