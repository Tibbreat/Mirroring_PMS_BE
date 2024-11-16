package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.SchoolYearInformation;
import sep490.g13.pms_be.model.request.school.AcademicInformationRequest;

@Repository
public interface SchoolYearInformationRepo extends JpaRepository<SchoolYearInformation, String> {

    @Query("SELECT s FROM SchoolYearInformation s WHERE  s.academicYear = :academicYear")
    SchoolYearInformation findByAcademicYear(String academicYear);

    @Modifying
    @Query("""
    UPDATE SchoolYearInformation s
    SET s.openingDay = :#{#request.openingDay},
        s.totalClassLevel1 = :#{#request.totalClassLevel1},
        s.totalStudentLevel1 = :#{#request.totalStudentLevel1},
        s.totalClassLevel2 = :#{#request.totalClassLevel2},
        s.totalStudentLevel2 = :#{#request.totalStudentLevel2},
        s.totalClassLevel3 = :#{#request.totalClassLevel3},
        s.totalStudentLevel3 = :#{#request.totalStudentLevel3},
        s.totalEnrolledStudents = :#{#request.totalEnrolledStudents},
        s.onlineEnrollmentStartDate = :#{#request.onlineEnrollmentStartDate},
        s.onlineEnrollmentEndDate = :#{#request.onlineEnrollmentEndDate},
        s.offlineEnrollmentStartDate = :#{#request.offlineEnrollmentStartDate},
        s.offlineEnrollmentEndDate = :#{#request.offlineEnrollmentEndDate},
        s.note = :#{#request.note} WHERE s.academicYear = :#{#request.academicYear}
    """)
    void updateSchoolYearInformation(AcademicInformationRequest request);

}
