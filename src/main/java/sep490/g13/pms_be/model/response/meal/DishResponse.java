package sep490.g13.pms_be.model.response.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponse {
    private String id;
    private String dishName;
    private String dishType;
}
