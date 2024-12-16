package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.model.response.classes.ClassListResponse;
import sep490.g13.pms_be.model.response.classes.ClassListResponseWithNumberChildren;
import sep490.g13.pms_be.model.response.classes.ListAvailableClassOption;
import sep490.g13.pms_be.model.response.classes.ListClassWithStudyStatus;
import sep490.g13.pms_be.model.response.kitchen.report.DailyReport;
import sep490.g13.pms_be.model.response.user.TeacherOfClassResponse;

import java.util.List;

@Repository
public interface ClassRepo extends JpaRepository<Classes, String> {

    @Query("SELECT new sep490.g13.pms_be.model.response.classes.ClassListResponseWithNumberChildren(" +
            "c.id, c.className, c.ageRange, c.openingDay, c.manager.id, c.manager.username, " +
            "c.academicYear, c.totalStudent, CAST(COUNT(cc.id) AS int), c.status) " +
            "FROM Classes c " +
            "LEFT JOIN c.manager " +
            "LEFT JOIN c.childrenClasses cc ON cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING  " +
            "WHERE c.academicYear = :academicYear " +
            "GROUP BY c.id, c.className, c.ageRange, c.openingDay, c.manager.id, c.manager.username, " +
            "c.academicYear, c.totalStudent, c.status " +
            "ORDER BY c.ageRange, c.className")
    List<ClassListResponseWithNumberChildren> findClassesByFilters(@Param("academicYear") String academicYear);

    @Query("SELECT c FROM Classes c " +
            "LEFT JOIN c.manager " +
            "LEFT JOIN c.childrenClasses cc ON cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING " +
            "WHERE c.academicYear = :academicYear " +
            "GROUP BY c.id, c.className, c.ageRange, c.openingDay, c.manager.id, c.manager.username, " +
            "c.academicYear, c.totalStudent, c.status " +
            "HAVING COUNT(cc.id) > 0")
    List<Classes> findClassesByAcademicYear(@Param("academicYear") String academicYear);


    @Query("SELECT new sep490.g13.pms_be.model.response.user.TeacherOfClassResponse(teacher.teacherId.id, teacher.teacherId.username, teacher.teacherId.fullName) " +
            "FROM ClassTeacher teacher " +
            "WHERE teacher.schoolClasses.id = :id")
    List<TeacherOfClassResponse> getTeacherOfClass(String id);

    @Query("SELECT c FROM Classes c " +
            "WHERE (:managerId IS NULL OR c.manager.id = :managerId) " +
            "AND (:teacherId IS NULL OR EXISTS (SELECT ct FROM ClassTeacher ct WHERE ct.schoolClasses.id = c.id AND ct.teacherId.id = :teacherId))")
    List<Classes> findClassesByTeacherIdOrManagerId(String teacherId, String managerId);


    @Query("SELECT new sep490.g13.pms_be.model.response.classes.ListAvailableClassOption(" +
            "c.id, c.className, c.ageRange, " +
            "ct.teacherId.fullName, ct.teacherId.username, " +
            "CAST(COUNT(cc.id) AS int), " +
            "c.totalStudent) " +
            "FROM Classes c " +
            "JOIN c.teachers ct " +
            "LEFT JOIN c.childrenClasses cc " +
            "WHERE c.academicYear = :academicYear AND c.manager.id = :managerId " +
            "AND c.status IN (sep490.g13.pms_be.utils.enums.ClassStatusEnums.NOT_STARTED, " +
            "                 sep490.g13.pms_be.utils.enums.ClassStatusEnums.IN_PROGRESS) " +
            "GROUP BY c.id, c.className, c.ageRange, ct.teacherId.fullName, ct.teacherId.username, c.totalStudent " +
            "HAVING c.totalStudent > COUNT(cc.id)")
    List<ListAvailableClassOption> listAvailableClassOptions(@Param("academicYear") String academicYear,
                                                             @Param("managerId") String managerId);

    @Query("SELECT new sep490.g13.pms_be.model.response.classes.ListAvailableClassOption(" +
            "c.id, c.className, c.ageRange, " +
            "ct.teacherId.fullName, ct.teacherId.username, " +
            "CAST(COUNT(cc.id) AS int), " +
            "c.totalStudent) " +
            "FROM Classes c " +
            "JOIN c.teachers ct " +
            "LEFT JOIN c.childrenClasses cc ON cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING " +
            "WHERE c.academicYear = :academicYear " +
            "AND c.status IN (sep490.g13.pms_be.utils.enums.ClassStatusEnums.NOT_STARTED, " +
            "                 sep490.g13.pms_be.utils.enums.ClassStatusEnums.IN_PROGRESS) " +
            "AND c.id NOT IN (" +
            "    SELECT cc.classes.id " +
            "    FROM ChildrenClass cc " +
            "    WHERE cc.children.id = :childrenId " +
            "    AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING" +
            ") " +
            "GROUP BY c.id, c.className, c.ageRange, ct.teacherId.fullName, ct.teacherId.username, c.totalStudent " +
            "HAVING c.totalStudent > COUNT(cc.id)")
    List<ListAvailableClassOption> listAvailableClassOptionsToTransfer(@Param("academicYear") String academicYear,
                                                                       @Param("childrenId") String childrenId);


    @Query("SELECT new sep490.g13.pms_be.model.response.classes.ListClassWithStudyStatus(" +
            "c.id, c.academicYear, c.className, c.status, cc.status, cc.countAbsent) " +
            "FROM Classes c " +
            "LEFT JOIN c.childrenClasses cc " +
            "WHERE cc.children.id = :childrenId " +
            "ORDER BY c.createdDate DESC")
    List<ListClassWithStudyStatus> findAllByChildrenId(String childrenId);

    @Query("SELECT DISTINCT c.academicYear FROM Classes c")
    List<String> listAllAcademicYears();

    @Query("SELECT new sep490.g13.pms_be.model.response.classes.ClassListResponse(" +
            "c.id, c.className, c.ageRange, c.openingDay, c.manager.id, c.manager.fullName, " +
            "c.academicYear, c.totalStudent, c.status) " +
            "FROM Classes c WHERE c.manager.id = :userId AND c.manager.role = 'CLASS_MANAGER'")
    List<ClassListResponse> findByManagerId(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE Classes c SET c.countChildrenRegisteredTransport = :count WHERE c.id = :classId")
    void updateCountStudentRegisteredTransport(@Param("classId") String classId, @Param("count") int count);

    @Modifying
    @Query("UPDATE Classes c SET c.countChildrenRegisteredOnBoarding = :count WHERE c.id = :classId")
    void updateCountStudentRegisteredOnBoarding(@Param("classId") String classId, @Param("count") int count);


    @Query("SELECT c.id FROM Classes c " +
            "WHERE c.academicYear = :academicYear " +
            "AND c.status = sep490.g13.pms_be.utils.enums.ClassStatusEnums.IN_PROGRESS")
    List<String> findClassIdsByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT COUNT(c.id) FROM Classes c " +
            "WHERE c.academicYear = :academicYear " +
            "AND c.ageRange = :ageRange " +
            "AND c.status IN (sep490.g13.pms_be.utils.enums.ClassStatusEnums.IN_PROGRESS, " +
            "                 sep490.g13.pms_be.utils.enums.ClassStatusEnums.NOT_STARTED)")
    int countClassesByAcademicYearAndAgeRange(String academicYear, String ageRange);

    @Query("SELECT new sep490.g13.pms_be.model.response.kitchen.report.DailyReport(" +
            "CAST(SUM(CASE WHEN c.ageRange = '3-4' THEN c.countChildrenRegisteredOnBoarding ELSE 0 END) AS int), " +
            "CAST(SUM(CASE WHEN c.ageRange = '4-5' THEN c.countChildrenRegisteredOnBoarding ELSE 0 END) AS int), " +
            "CAST(SUM(CASE WHEN c.ageRange = '5-6' THEN c.countChildrenRegisteredOnBoarding ELSE 0 END) AS int)) " +
            "FROM Classes c " +
            "WHERE c.academicYear = :academicYear")
    DailyReport countChildrenByAgeRange(@Param("academicYear") String academicYear);

    @Query("SELECT (COUNT(c.id) > 0) FROM Classes c " +
            "WHERE c.academicYear = :academicYear " +
            "AND c.className = :className")
    boolean checkClassExits(String className, String academicYear);

}
