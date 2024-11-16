package sep490.g13.pms_be.model.request.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcademicInformationRequest {
    private String academicYear;

    private String openingDay;

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

    private String note;

    private List<AdmissionFile> admissionFiles;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdmissionFile {
        private String fileName;
        private String note;
    }
}
