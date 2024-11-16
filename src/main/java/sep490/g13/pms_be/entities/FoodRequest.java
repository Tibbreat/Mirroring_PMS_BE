package sep490.g13.pms_be.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest extends Auditable<String> {
    private String dayNeeded;

    private String status;

    private String contractNumber;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private FoodServiceProvider foodServiceProvider;

    @ManyToOne
    private User receiver;
}

