package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sep490.g13.pms_be.utils.enums.ClassStatusEnums;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classes extends Auditable<String> {
    private String className;

    private String ageRange;

    private Date openingDay;

    private String academicYear;

    private int totalStudent;

    private int countChildrenRegisteredTransport;

    private int countChildrenRegisteredOnBoarding;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ChildrenClass> childrenClasses = new HashSet<>();


    @OneToMany(mappedBy = "schoolClasses", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ClassTeacher> teachers = new HashSet<>();

    @ManyToOne
    private User manager;

    @Enumerated(EnumType.STRING)
    private ClassStatusEnums status ;

    public void setAcademicYear() {
        if (this.academicYear == null || this.academicYear.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            this.academicYear = currentYear + "-" + (currentYear + 1);
        }
    }

    @PrePersist
    private void onPrePersist() {
        setAcademicYear();
    }
}
