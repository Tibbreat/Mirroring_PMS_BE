package sep490.g13.pms_be.model.request.meal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AddMealRequest {

    private MealDetails group3To4;
    private MealDetails group4To5;
    private MealDetails group5To6;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @Getter
    @Setter
    public static class MealDetails {
        private List<DishDetail> mainDishes;
        private List<DishDetail> sideDishes;
        private String snack;
    }

    @Getter
    @Setter
    public static class DishDetail {
        private String mainDishes;
        private String sideDishes;
    }
}
