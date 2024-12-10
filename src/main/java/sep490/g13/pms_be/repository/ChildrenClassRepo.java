package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.ChildrenClass;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.utils.enums.StudyStatusEnums;

import java.util.List;

@Repository
public interface ChildrenClassRepo extends JpaRepository<ChildrenClass, String> {

    @Query("SELECT COUNT(cc.id) " +
            "FROM ChildrenClass cc " +
            "WHERE cc.classes.id = :classId " +
            "AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING ")
    int countChildrenByClassId(String classId);

    @Query("SELECT COUNT(cc.id) FROM ChildrenClass cc WHERE cc.classes.id = :classId AND cc.children.isDisabled = true")
    int countDisabledChildrenByClassId(String classId);

    @Query("SELECT cc.classes FROM ChildrenClass cc" +
            " WHERE cc.children.id = :childrenId " +
            "AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING ")
    Classes findClassByChildrenId(String childrenId);

    @Query("SELECT cc.classes FROM ChildrenClass cc WHERE cc.children.id IN :childrenIds")
    List<Classes> findAllClassByChildrenId(List<String> childrenIds);

    @Query("SELECT COUNT(ch.id) " +
            "FROM ChildrenClass cc " +
            "JOIN cc.children ch " +
            "JOIN ch.vehicle v " +
            "WHERE cc.classes.id = :classId " +
            "AND ch.isRegisteredForTransport = true " +
            "AND v.transport.id = :transportProviderId")
    int countStudentRegisteredTransport(String classId, String transportProviderId);

    @Modifying
    @Query("UPDATE ChildrenClass cc " +
            "SET cc.status = :status " +
            "WHERE cc.children.id = :childrenId " +
            "AND cc.classes.id = :classId")
    void updateStatusByChildrenIdAndClassId(String childrenId, String classId, StudyStatusEnums status);

    @Query("SELECT cc FROM ChildrenClass cc " +
            "WHERE cc.children.id = :childrenId " +
            "AND cc.status = sep490.g13.pms_be.utils.enums.StudyStatusEnums.STUDYING ")
    ChildrenClass findByChildrenId(String childrenId);

    @Query("SELECT cc FROM ChildrenClass cc " +
            "WHERE cc.children.id = :childrenId " +
            "AND cc.classes.id = :classId " +
            "AND cc.status = :status")
    ChildrenClass findByChildrenIdAndClassesId(String childrenId, String classId, StudyStatusEnums status);

}
