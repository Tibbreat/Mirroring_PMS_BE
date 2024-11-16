package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish extends Auditable<String> {
    private String dishName;
    private String dishType;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonBackReference
    private Meal meal;
}
