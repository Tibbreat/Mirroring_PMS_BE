package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.entities.Dish;
import sep490.g13.pms_be.entities.Meal;
import sep490.g13.pms_be.model.request.meal.AddMealRequest;
import sep490.g13.pms_be.model.response.meal.DishResponse;
import sep490.g13.pms_be.model.response.meal.MealResponse;
import sep490.g13.pms_be.repository.DishRepo;
import sep490.g13.pms_be.repository.MealRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {
    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private DishRepo dishRepo;

    public void save(AddMealRequest request) {
        Date mealDate = request.getDate();
        List<Meal> meals = new ArrayList<>();

        meals.add(createMeal("3-4", request.getGroup3To4(), mealDate));
        meals.add(createMeal("4-5", request.getGroup4To5(), mealDate));
        meals.add(createMeal("5-6", request.getGroup5To6(), mealDate));

        mealRepo.saveAll(meals);
    }

    public List<MealResponse> getMealResponsesByDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = formatter.parse(date);

        List<Meal> meals = mealRepo.findAllByMealDate(parsedDate);
        return meals.stream()
                .map(this::convertToMealResponse)
                .collect(Collectors.toList());
    }

    public List<MealResponse> getMealResponsesByMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Meal> meals = mealRepo.findByMealDateBetween(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
        return meals.stream()
                .map(this::convertToMealResponse)
                .collect(Collectors.toList());
    }

    public List<MealResponse> getMealResponsesForCurrentWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);

        // Tìm tất cả Meal từ startOfWeek đến endOfWeek
        List<Meal> meals = mealRepo.findByMealDateBetween(
                java.sql.Date.valueOf(startOfWeek),
                java.sql.Date.valueOf(endOfWeek)
        );

        return meals.stream()
                .map(this::convertToMealResponse)
                .collect(Collectors.toList());
    }
    private MealResponse convertToMealResponse(Meal meal) {
        List<DishResponse> dishResponses = meal.getDishes().stream()
                .map(dish -> new DishResponse(dish.getId(), dish.getDishName(), dish.getDishType()))
                .collect(Collectors.toList());

        return new MealResponse(
                meal.getId(),
                meal.getAgeRange(),
                meal.getMealDate(),
                dishResponses
        );
    }


    private Meal createMeal(String ageGroup, AddMealRequest.MealDetails mealDetails, Date mealDate) {
        // Tạo bữa ăn cho nhóm tuổi cụ thể với ngày từ request
        Meal meal = new Meal();
        meal.setAgeRange(ageGroup);
        meal.setMealDate(mealDate);

        Meal savedMeal = mealRepo.save(meal);

        saveDishes(savedMeal, mealDetails.getMainDishes(), "main");
        saveDishes(savedMeal, mealDetails.getSideDishes(), "side");
        saveSnack(savedMeal, mealDetails.getSnack());

        return savedMeal;
    }

    private void saveDishes(Meal meal, List<AddMealRequest.DishDetail> dishDetails, String type) {
        if (dishDetails == null || dishDetails.isEmpty()) {
            return;
        }
        for (AddMealRequest.DishDetail dishDetail : dishDetails) {
            String dishName = type.equals("main") ? dishDetail.getMainDishes() : dishDetail.getSideDishes();
            if (dishName != null && !dishName.isEmpty()) {
                Dish dish = new Dish();
                dish.setDishName(dishName);
                dish.setDishType(type);
                dish.setMeal(meal);
                dishRepo.save(dish);
            }
        }
    }

    private void saveSnack(Meal meal, String snack) {
        if (snack != null && !snack.isEmpty()) {
            Dish snackDish = new Dish();
            snackDish.setDishName(snack);
            snackDish.setDishType("snack");
            snackDish.setMeal(meal);
            dishRepo.save(snackDish);
        }
    }
}
