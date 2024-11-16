package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolYearInformation extends Auditable<String> {

    @ManyToOne
    private School school;

    @Column(unique = true)
    private String academicYear;

    private String openingDay;
    private String closingDay;

    private int totalClassLevel1;
    private int totalStudentLevel1;

    private int totalClassLevel2;
    private int totalStudentLevel2;

    private int totalClassLevel3;
    private int totalStudentLevel3;

    private int totalEnrolledStudents;

    private LocalDate onlineEnrollmentStartDate;
    private LocalDate onlineEnrollmentEndDate;

    private LocalDate offlineEnrollmentStartDate;
    private LocalDate offlineEnrollmentEndDate;

    @Lob
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "schoolYearInformationId")
    @JsonManagedReference
    private Set<AdmissionFile> admissionFiles;
}
