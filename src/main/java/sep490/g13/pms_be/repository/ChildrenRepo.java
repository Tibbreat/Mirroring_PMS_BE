package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.Children;
import org.springframework.data.domain.Pageable;

import sep490.g13.pms_be.model.response.children.*;

import java.util.List;

@Repository
public interface ChildrenRepo extends JpaRepository<Children, String> {

    @Query("SELECT DISTINCT new sep490.g13.pms_be.model.response.children.ChildrenListResponse(" +
            "ch.id, ch.childName, ch.childBirthDate, sc.id, sc.classes.className, ch.imageUrl, ch.gender, " +
            "(SELECT rlFather.parentId.fullName FROM Relationship rlFather WHERE rlFather.childrenId.id = ch.id AND rlFather.relationship = 'Father'), " +
            "(SELECT rlMother.parentId.fullName FROM Relationship rlMother WHERE rlMother.childrenId.id = ch.id AND rlMother.relationship = 'Mother')) " +
            "FROM Children ch " +
            "LEFT JOIN ch.childrenClasses sc " +
            "WHERE (:academicYear IS NULL OR sc.academicYear = :academicYear) " +
            "AND (:childName IS NULL OR LOWER(ch.childName) LIKE LOWER(CONCAT('%', :childName, '%'))) " +
            "AND sc.status NOT IN (sep490.g13.pms_be.utils.enums.StudyStatusEnums.MOVED_OUT) " +
            "ORDER BY ch.childName")
    Page<ChildrenListResponse> findChildrenByFilter(
            @Param("academicYear") String academicYear,
            @Param("childName") String childName,
            Pageable pageable);


    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenDetailResponse(" +
            "ch.childName, ch.childBirthDate, ch.childAddress, ch.isRegisteredForTransport, ch.isRegisteredForBoarding, ch.imageUrl, ch.birthAddress, " +
            "ch.nationality, ch.religion, ch.gender, " +
            "father.parentId.id, father.parentId.fullName, father.parentId.phone, " +
            "mother.parentId.id, mother.parentId.fullName, mother.parentId.phone) " +
            "FROM Children ch " +
            "LEFT JOIN Relationship father ON father.childrenId.id = ch.id AND father.relationship = 'Father' " +
            "LEFT JOIN Relationship mother ON mother.childrenId.id = ch.id AND mother.relationship = 'Mother' " +
            "WHERE ch.id = :childrenId")
    ChildrenDetailResponse findChildrenDetailById(@Param("childrenId") String childrenId);

    @Modifying
    @Query("UPDATE Children ch SET ch.isRegisteredForTransport = :status WHERE ch.id = :childrenId")
    void updateTransportServiceStatus(@Param("childrenId") String childrenId, @Param("status") Boolean status);

    @Modifying
    @Query("UPDATE Children ch SET ch.isRegisteredForBoarding = :status WHERE ch.id = :childrenId")
    void updateBoardingServiceStatus(@Param("childrenId") String childrenId, @Param("status") Boolean status);

    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenListResponse(" +
            "ch.id, ch.childName, ch.childBirthDate, sc.id,sc.classes.className, ch.imageUrl, ch.gender, " +
            "(SELECT rlFather.parentId.fullName FROM Relationship rlFather WHERE rlFather.childrenId.id = ch.id AND rlFather.relationship = 'Father'), " +
            "(SELECT rlMother.parentId.fullName FROM Relationship rlMother WHERE rlMother.childrenId.id = ch.id AND rlMother.relationship = 'Mother')) " +
            "FROM Children ch " +
            "LEFT JOIN ch.childrenClasses sc " +
            "WHERE  sc.classes.id = :classId " +
            "AND sc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING " +
            "ORDER BY ch.childName")
    Page<ChildrenListResponse> findChildrenByClass(
            @Param("classId") String classId,
            Pageable pageable);

    @Query("SELECT ch FROM Children ch " +
            "LEFT JOIN ch.childrenClasses sc " +
            "WHERE  sc.classes.id = :classId " +
            "AND sc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING " +
            "ORDER BY ch.childName")
    List<Children> findChildrenByClassId(String classId);

    @Query("SELECT c FROM Children c " +
            "LEFT JOIN c.childrenClasses cc ON c.id = cc.children.id " +
            "WHERE cc.classes.id = :classId " +
            "AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING")
    List<Children> findChildrenByClass(@Param("classId") String classId);

    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenListByRoute(" +
            "ch.id, ch.childName, sc.classes.className, ch.imageUrl, v.vehicleName, sl.locationName, ch.gender) " +
            "FROM Children ch " +
            "JOIN ch.childrenClasses sc " +
            "JOIN ch.vehicle v " +
            "JOIN ch.registeredStopLocation sl " +
            "JOIN ch.childrenRoutes cr " +
            "WHERE cr.route.id = :routeId " +
            "AND sc.status <> sep490.g13.pms_be.utils.enums.StudyStatusEnums.MOVED_OUT " +
            "ORDER BY sl.stopOrder")
    List<ChildrenListByRoute> findChildrenByRouteId(@Param("routeId") String routeId);


    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenDataSheetRow(" +
            "ch.childName, " +
            "ch.childBirthDate, " +
            "ch.gender, " +
            "ch.nationality, " +
            "ch.religion, " +
            "ch.birthAddress, " +
            "ch.childAddress, " +
            "(SELECT rlFather.parentId.fullName FROM Relationship rlFather WHERE rlFather.childrenId.id = ch.id AND rlFather.relationship = 'Father'), " +
            "(SELECT rlMother.parentId.fullName FROM Relationship rlMother WHERE rlMother.childrenId.id = ch.id AND rlMother.relationship = 'Mother')) " +
            "FROM Children ch " +
            "LEFT JOIN ch.childrenClasses sc " +
            "WHERE sc.classes.id = :classId " +
            "ORDER BY ch.childName")
    List<ChildrenDataSheetRow> getChildrenDataSheet(String classId);

    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenDataSheetByAcademic(" +
            "ch.childName, " +
            "ch.childBirthDate, " +
            "ch.gender, " +
            "ch.nationality, " +
            "ch.religion, " +
            "ch.birthAddress, " +
            "ch.childAddress, " +
            "(SELECT rlFather.parentId.fullName FROM Relationship rlFather WHERE rlFather.childrenId.id = ch.id AND rlFather.relationship = 'Father'), " +
            "(SELECT rlMother.parentId.fullName FROM Relationship rlMother WHERE rlMother.childrenId.id = ch.id AND rlMother.relationship = 'Mother'), " +
            "cl.className) " +
            "FROM Children ch " +
            "JOIN ch.childrenClasses cc " +
            "JOIN cc.classes cl " +
            "WHERE cc.academicYear = :academicYear " +
            "ORDER BY cl.className, ch.childName")
    List<ChildrenDataSheetByAcademic> getChildrenDataSheetByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenDataSheetByVehicle(" +
            "ch.id, ch.childName, sc.classes.className, ch.imageUrl, v.vehicleName, sl.locationName, ch.gender) " +
            "FROM Children ch " +
            "JOIN ch.childrenClasses sc " +
            "JOIN ch.vehicle v " +
            "JOIN ch.registeredStopLocation sl " +
            "JOIN ch.childrenRoutes cr " +
            "WHERE v.id = :vehicleId " +
            "AND sc.status <> sep490.g13.pms_be.utils.enums.StudyStatusEnums.MOVED_OUT " +
            "ORDER BY sl.stopOrder")
    List<ChildrenDataSheetByVehicle> getChildrenDataSheetByVehicle(@Param("vehicleId") String vehicleId);

    @Modifying
    @Query("UPDATE Children ch " +
            "SET ch.vehicle = null, " +
            "    ch.registeredStopLocation = null " +
            "WHERE ch.id = :childrenId")
    void removeVehicle(@Param("childrenId") String childrenId);

    @Modifying
    @Query("UPDATE Children ch SET ch.isRegisteredForTransport = false, ch.vehicle = NULL WHERE ch.vehicle.id = :vehicleId")
    void cancelVehicleRegistration(@Param("vehicleId") String vehicleId);

    @Query("SELECT new sep490.g13.pms_be.model.response.children.ChildrenOptionResponse("
            + "c.id, c.childName, cl.className, p.id, p.fullName) "
            + "FROM Children c "
            + "JOIN c.childrenClasses cc "
            + "JOIN cc.classes cl "
            + "JOIN cl.teachers ct "
            + "JOIN ct.teacherId t "
            + "JOIN c.relationships r "
            + "JOIN r.parentId p "
            + "WHERE t.id = :teacherId "
            + "AND p.username IS NOT NULL"
    )
    List<ChildrenOptionResponse> findChildrenByTeacherId(String teacherId);

    @Query("SELECT c.id FROM Children c WHERE c.vehicle.id IN :vehicleIds")
    List<String> findAllChildrenByVehicleId(List<String> vehicleIds);
}
