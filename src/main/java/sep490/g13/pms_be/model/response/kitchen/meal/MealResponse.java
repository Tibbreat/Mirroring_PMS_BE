package sep490.g13.pms_be.model.response.kitchen.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealResponse {
    private String id;
    private String ageRange;
    private Date mealDate;
    private List<DishResponse> dishes;
}
