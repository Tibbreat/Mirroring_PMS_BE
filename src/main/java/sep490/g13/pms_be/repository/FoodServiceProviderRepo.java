package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sep490.g13.pms_be.entities.FoodServiceProvider;

public interface FoodServiceProviderRepo extends JpaRepository<FoodServiceProvider, String> {

    @Query("SELECT fsp FROM FoodServiceProvider fsp")
    Page<FoodServiceProvider> getAllProvider(Pageable pageable);

    @Modifying
    @Query("UPDATE FoodServiceProvider fsp SET fsp.isActive = :isActive WHERE fsp.id = :providerId")
    void updateProviderStatus(String providerId, boolean isActive);
}
