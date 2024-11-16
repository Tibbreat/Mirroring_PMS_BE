package sep490.g13.pms_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Children extends Auditable<String> {
    private String childName;
    private LocalDate childBirthDate;
    private String childAddress;
    private String birthAddress;
    private Boolean isRegisteredForTransport = Boolean.FALSE;
    private Boolean isRegisteredForBoarding = Boolean.FALSE;

    private String imageUrl;

    private String cloudinaryImageId;

    private String nationality;
    private String religion;
    private String gender;

    private Boolean isDisabled;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "children")
    private Set<ChildrenClass> childrenClasses;

    @OneToMany(mappedBy = "childrenId", cascade = CascadeType.ALL)
    private Set<Relationship> relationships;

    @OneToMany(mappedBy = "children")
    private Set<ChildrenRoute> childrenRoutes;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private StopLocation registeredStopLocation;
}
