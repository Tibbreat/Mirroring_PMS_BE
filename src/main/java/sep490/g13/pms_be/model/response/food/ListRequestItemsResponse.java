package sep490.g13.pms_be.model.response.food;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListRequestItemsResponse {
    private String itemId;
    private String itemName;
    private String itemQuantity;
    private String note;
}
