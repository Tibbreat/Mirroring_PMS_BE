package sep490.g13.pms_be.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sep490.g13.pms_be.entities.ClassTeacher;
import sep490.g13.pms_be.entities.Classes;

import java.util.List;


@Repository
public interface ClassTeacherRepo extends CrudRepository<ClassTeacher, String> {

    @Query("SELECT ct.schoolClasses FROM ClassTeacher ct WHERE ct.teacherId.id = :teacherId")
    List<Classes> findClassesByTeacherId(String teacherId);

    @Query("SELECT ct FROM ClassTeacher ct WHERE ct.teacherId.id = :teacherId AND ct.schoolClasses.academicYear = :schoolYear")
    List<ClassTeacher> findByTeacherIdAndSchoolYear(String teacherId, String schoolYear);

}

