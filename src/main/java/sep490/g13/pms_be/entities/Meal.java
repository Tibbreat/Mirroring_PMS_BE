package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal extends Auditable<String> {
    private String ageRange;

    @Temporal(TemporalType.DATE)
    private Date mealDate;

    @OneToMany(mappedBy = "meal")
    @JsonManagedReference
    private List<Dish> dishes;
}
