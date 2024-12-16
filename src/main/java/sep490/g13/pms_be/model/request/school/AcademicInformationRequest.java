package sep490.g13.pms_be.model.request.school;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Năm học không được để trống")
    private String academicYear;

    @NotEmpty(message = "Ngày khai giảng không được để trống")
    private String openingDay;

    @NotEmpty(message = "Số lớp 3-4 tuổi dự kiến mở không được để trống")
    private int totalClassLevel1;
    private int totalStudentLevel1;

    @NotEmpty(message = "Số lớp 4-5 tuổi dự kiến mở không được để trống")
    private int totalClassLevel2;
    private int totalStudentLevel2;

    @NotEmpty(message = "Số lớp 5-6 tuổi dự kiến mở không được để trống")
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
