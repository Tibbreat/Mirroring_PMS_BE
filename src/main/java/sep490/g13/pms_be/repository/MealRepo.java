package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Meal;

import java.util.Date;
import java.util.List;

@Repository
public interface MealRepo extends JpaRepository<Meal, String> {
    List<Meal> findAllByMealDate(Date mealDate);

    List<Meal> findByMealDateBetween(Date startDate, Date endDate);
}
