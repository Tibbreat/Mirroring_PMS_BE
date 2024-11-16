package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import sep490.g13.pms_be.utils.enums.StudyStatusEnums;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildrenClass extends Auditable<String> {

    @ManyToOne
    @JoinColumn(name = "children_id")
    @JsonBackReference
    private Children children;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private Classes classes;

    private String academicYear;

    private int countAbsent;

    private StudyStatusEnums status;
}
