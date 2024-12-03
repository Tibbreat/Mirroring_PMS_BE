package sep490.g13.pms_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.model.response.teacher.TeacherOptionResponse;
import sep490.g13.pms_be.model.response.user.GetParentOptionResponse;
import sep490.g13.pms_be.model.response.user.GetUsersOptionResponse;
import sep490.g13.pms_be.utils.enums.RoleEnums;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByUsername(String username);

    @Query("SELECT r.parentId FROM Relationship r " +
            "WHERE r.childrenId.id = :childrenId " +
            "AND r.relationship = :relationship")
    User findByRelationship(String childrenId, String relationship);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findByRole(RoleEnums role);

    @Query("SELECT COUNT(u.id) FROM User u WHERE u.username LIKE %:accountName%")
    int countByUsernameContaining(String accountName);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:roles IS NULL OR u.role IN :roles) AND " +
            "(:isActive IS NULL OR u.isActive = :isActive) AND " +
            "(:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))")
    Page<User> getUsersByRoles(@Param("roles") List<RoleEnums> roles,
                               @Param("isActive") Boolean isActive,
                               @Param("fullName") String fullName,
                               Pageable pageable);



    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = :status WHERE u.id = :id")
    int updateUserStatus(@Param("id") String id, @Param("status") Boolean status);

    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetUsersOptionResponse(u.id, u.username) FROM User u " +
            "WHERE u.role = :role " +
            "AND u.isActive = true")
    List<GetUsersOptionResponse> findAllByRole(RoleEnums role);

    @Query("SELECT u FROM User u WHERE u.idCardNumber = :idCardNumber")
    User existByIdCardNumber(String idCardNumber);

    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetUsersOptionResponse(u.id, u.username) FROM User u " +
            "WHERE u.role = :role " +
            "AND u.isActive = true " +
            "AND u.username IS NOT NULL")
    List<GetUsersOptionResponse> findAllByRoleWithUserName(RoleEnums role);

    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetParentOptionResponse(p.id, p.username, p.fullName) " +
            "FROM Relationship r " +
            "JOIN r.parentId p " +
            "JOIN r.childrenId ch " +
            "JOIN ch.childrenClasses cc " +
            "JOIN cc.classes c " +
            "JOIN c.teachers ct " +
            "JOIN ct.teacherId t " +
            "WHERE t.id = :teacherId " +
            "AND p.username IS NOT NULL " +
            "AND p.username <> ''")
    List<GetParentOptionResponse> getParentsByTeacher(@Param("teacherId") String teacherId);


    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetUsersOptionResponse(u.id, u.username) " +
            "FROM User u " +
            "WHERE u.role = 'TEACHER' " +
            "AND u.id NOT IN (" +
            "    SELECT ct.teacherId.id " +
            "    FROM Classes c " +
            "    JOIN c.teachers ct " +
            "    WHERE c.academicYear = :academicYear" +
            ")")
    List<GetUsersOptionResponse> getAvailableTeacherInAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT new sep490.g13.pms_be.model.response.teacher.TeacherOptionResponse("
            + "t.id, cl.className, t.fullName) "
            + "FROM Children c "
            + "JOIN c.childrenClasses cc "
            + "JOIN cc.classes cl "
            + "JOIN cl.teachers ct "
            + "JOIN ct.teacherId t "
            + "JOIN c.relationships r "
            + "JOIN r.parentId p "
            + "WHERE p.id = :parentId"
            + " AND p.username IS NOT NULL"
    )
    List<TeacherOptionResponse> findTeachersByParentId(String parentId);


    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetUsersOptionResponse(u.id, u.username) " +
            "FROM User u LEFT JOIN Vehicle v ON u = v.manager " +
            "WHERE u.role = 'TRANSPORT_MANAGER' " +
            "AND u.isActive = true " +
            "AND v IS NULL")
    List<GetUsersOptionResponse> findTransportManagerAvailable();

    @Query("SELECT new sep490.g13.pms_be.model.response.user.GetParentOptionResponse(p.id, p.username, p.fullName) " +
            "FROM Relationship r " +
            "JOIN r.parentId p " +
            "JOIN r.childrenId ch " +
            "WHERE ch.id = :childrenId " +
            "AND p.username IS NOT NULL ")
    GetParentOptionResponse findParentByChildrenId(String childrenId);
}