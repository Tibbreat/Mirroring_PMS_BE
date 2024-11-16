package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.model.request.meal.AddMealRequest;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.model.response.meal.MealResponse;
import sep490.g13.pms_be.service.entity.MealService;

import java.text.ParseException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/pms/meal")
public class MealController {

    @Autowired
    private MealService mealService;


    @PostMapping("/create")
    public ResponseEntity<ResponseModel<Void>> createMeal(@RequestBody AddMealRequest request) {
        mealService.save(request);
        return ResponseEntity.ok(
                ResponseModel.<Void>builder()
                        .message("Meal created successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ResponseModel<List<MealResponse>>> getMealsByDate(@PathVariable String date) {
        try {
            List<MealResponse> mealResponses = mealService.getMealResponsesByDate(date);

            return ResponseEntity.ok(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Success")
                            .data(mealResponses)
                            .build()
            );
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Invalid date format. Please use yyyy-MM-dd.")
                            .build()
            );
        }
    }

    @GetMapping("/month/{yearMonth}")
    public ResponseEntity<ResponseModel<List<MealResponse>>> getMealsByMonth(@PathVariable String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            List<MealResponse> mealResponses = mealService.getMealResponsesByMonth(ym);

            return ResponseEntity.ok(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Success")
                            .data(mealResponses)
                            .build()
            );
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Invalid month format. Please use yyyy-MM format.")
                            .build()
            );
        }
    }

    @GetMapping("/week")
    public ResponseEntity<ResponseModel<List<MealResponse>>> getMealsByWeek() {
        try {
            List<MealResponse> mealResponses = mealService.getMealResponsesForCurrentWeek();
            return ResponseEntity.ok(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Success")
                            .data(mealResponses)
                            .build()
            );
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(
                    ResponseModel.<List<MealResponse>>builder()
                            .message("Invalid month format. Please use yyyy-MM format.")
                            .build()
            );
        }
    }
}
