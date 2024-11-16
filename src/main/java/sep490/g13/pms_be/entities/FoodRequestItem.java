package sep490.g13.pms_be.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodRequestItem extends Auditable<String> {
    private String foodName;
    private String quantity;
    private String note;



    @ManyToOne
    @JoinColumn(name = "food_request_id")
    private FoodRequest foodRequest;
}
