package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.AttendanceLog;
import sep490.g13.pms_be.model.response.attendance.ChildrenAttendanceLog;
import sep490.g13.pms_be.utils.enums.AttendanceStatusEnums;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceLog, String> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM AttendanceLog a " +
            "WHERE a.attendanceDate = :today AND a.classes.id = :classId")
    boolean existsByAttendanceDate(@Param("today") LocalDate today, @Param("classId") String classId);


    @Query("SELECT new sep490.g13.pms_be.model.response.attendance.ChildrenAttendanceLog(" +
            "c.id,a.id, c.imageUrl, c.childName, a.morningBoardingTime, a.morningAlightingTime, a.afternoonBoardingTime, a.afternoonAlightingTime,  a.status, a.note) " +
            "FROM AttendanceLog a " +
            "JOIN a.children c " +
            "WHERE a.classes.id = :classId " +
            "AND a.attendanceDate = :today")
    List<ChildrenAttendanceLog> getChildrenAttendanceLog(@Param("classId") String classId, @Param("today") LocalDate today );

    @Modifying
    @Query("UPDATE AttendanceLog a SET a.status = :status, a.note = :note " +
            "WHERE a.children.id = :childrenId " +
            "AND a.attendanceDate = :today " +
            "AND a.classes.id = :classId")
    void updateAttendanceStatus(String childrenId, AttendanceStatusEnums status, LocalDate today, String classId, String note);


    @Query("SELECT new sep490.g13.pms_be.model.response.attendance.ChildrenAttendanceLog(" +
            "c.id,a.id, c.imageUrl, c.childName, a.morningBoardingTime, a.morningAlightingTime, a.afternoonBoardingTime, a.afternoonAlightingTime,  a.status, a.note) " +
            "FROM AttendanceLog a " +
            "JOIN a.children c " +
            "WHERE c.vehicle.manager.id = :managerId " +
            "AND a.attendanceDate = :today")
    List<ChildrenAttendanceLog> getChildrenAttendanceLogBaseOnVehicle(String managerId, LocalDate today);

    @Query("SELECT a.children.id " +
            "FROM AttendanceLog a " +
            "WHERE a.attendanceDate = :today " +
            "AND a.status = 'ABSENT'")
    List<String> getAllChildrenAbsentInDay(LocalDate today);
}
