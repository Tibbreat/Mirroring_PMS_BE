package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.entities.ClassTeacher;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.model.response.classes.ClassOption;
import sep490.g13.pms_be.model.response.user.TeacherOfClassResponse;
import sep490.g13.pms_be.repository.ClassRepo;
import sep490.g13.pms_be.repository.ClassTeacherRepo;
import sep490.g13.pms_be.repository.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassTeacherService {
    @Autowired
    private ClassTeacherRepo classTeacherRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClassRepo classesRepo;


    public void addTeacherIntoClass(String classId, String teacherId) {
        Classes schoolClass = classesRepo.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found: " + classId));

        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));

        ClassTeacher classTeacher = new ClassTeacher();
        classTeacher.setSchoolClasses(schoolClass);
        classTeacher.setTeacherId(teacher);
        classTeacherRepo.save(classTeacher);
    }


    public Page<Classes> getClassByTeacherId(String teacherId, int size, int page){
        Pageable pageable = PageRequest.of(page, size);
        return classTeacherRepo.findClassesByTeacherId(teacherId, pageable);
    }
    public List<ClassOption> getClassesByTeacherAndSchoolYear(String teacherId, String schoolYear) {
        List<ClassTeacher> classTeachers = classTeacherRepo.findByTeacherIdAndSchoolYear(teacherId, schoolYear);

        return classTeachers.stream()
                .map(ct -> ClassOption.builder()
                        .id(ct.getSchoolClasses().getId())
                        .name(ct.getSchoolClasses().getClassName())
                        .build())
                .collect(Collectors.toList());
    }


}
