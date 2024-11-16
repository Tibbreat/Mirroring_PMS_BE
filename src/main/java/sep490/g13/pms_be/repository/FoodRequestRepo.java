package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.FoodRequest;
import sep490.g13.pms_be.model.response.food.ListFoodResponse;

@Repository
public interface FoodRequestRepo extends JpaRepository<FoodRequest, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.food.ListFoodResponse(" +
            "f.id, " +
            "f.dayNeeded, " +
            "u.username, " +
            "f.createdDate, " +
            "f.status) " +
            "FROM FoodRequest f " +
            "JOIN User u ON u.id = f.createdBy " +
            "WHERE f.foodServiceProvider.id = :providerId " +
            "ORDER BY f.createdDate DESC")
    Page<ListFoodResponse> getAllByProviderId(String providerId, Pageable pageable);

    @Modifying
    @Query("UPDATE FoodRequest f SET f.status = :status, f.contractNumber = :contractNumber " +
            "WHERE f.id = :foodRequestId")
    void changeStatusOfRequest(String foodRequestId, String status, String contractNumber);
}
