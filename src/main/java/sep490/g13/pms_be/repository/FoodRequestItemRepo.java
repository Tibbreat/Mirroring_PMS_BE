package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.FoodRequestItem;
import sep490.g13.pms_be.model.response.food.ListRequestItemsResponse;

import java.util.List;

@Repository
public interface FoodRequestItemRepo extends JpaRepository<FoodRequestItem, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.food.ListRequestItemsResponse(f.id, f.foodName, f.quantity, f.note) FROM FoodRequestItem f WHERE f.foodRequest.id = :foodRequestId")
    List<ListRequestItemsResponse> findByFoodRequestId(String foodRequestId);



}
