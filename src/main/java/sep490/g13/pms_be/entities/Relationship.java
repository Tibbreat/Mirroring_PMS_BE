package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relationship extends Auditable<String> {

    @ManyToOne
    @JoinColumn(name = "children_id", nullable = false)
    @JsonIgnore
    private Children childrenId;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    @JsonIgnore
    private User parentId;

    @Column(name = "relationship", nullable = false)
    private String relationship;
}
