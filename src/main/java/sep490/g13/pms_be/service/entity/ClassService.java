package sep490.g13.pms_be.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.Children;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.entities.SchoolYearInformation;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.exception.other.PermissionNotAcceptException;
import sep490.g13.pms_be.model.request.classes.AddClassRequest;
import sep490.g13.pms_be.model.response.classes.*;
import sep490.g13.pms_be.model.response.kitchen.report.DailyReport;
import sep490.g13.pms_be.model.response.user.TeacherOfClassResponse;
import sep490.g13.pms_be.repository.*;
import sep490.g13.pms_be.utils.enums.ClassStatusEnums;
import sep490.g13.pms_be.utils.enums.RoleEnums;
import sep490.g13.pms_be.utils.enums.StudyStatusEnums;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassService {
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClassTeacherService classTeacherService;

    @Autowired
    private SchoolYearInformationRepo schoolYearInformationRepo;

    @Autowired
    private ChildrenRepo childrenRepo;

    @Autowired
    private ChildrenClassRepo childrenClassRepo;

    public Classes createNewClass(AddClassRequest classRequest) {
        Classes newClass = new Classes();
        BeanUtils.copyProperties(classRequest, newClass);
        User manager = validateManager(classRequest.getManagerId());
        newClass.setManager(manager);
        newClass.setStatus(ClassStatusEnums.IN_PROGRESS);
        validateCreatedBy(classRequest.getCreatedBy());

        SchoolYearInformation academicYearInformation = schoolYearInformationRepo.findByAcademicYear(
                classRequest.getAcademicYear());

        newClass.setCountChildrenRegisteredOnBoarding(0);
        newClass.setCountChildrenRegisteredTransport(0);
        switch (classRequest.getAgeRange()) {
            case "3-4":
                int maxClassLevel_1 = academicYearInformation.getTotalClassLevel1();
                if(classRepo.countClassesByAcademicYearAndAgeRange(classRequest.getAcademicYear(), "3-4") >= maxClassLevel_1) {
                    throw new PermissionNotAcceptException("Số lớp 3-4 đã đạt tối đa");
                }
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel1());
                break;
            case "4-5":
                int maxClassLevel_2 = academicYearInformation.getTotalClassLevel2();
                if(classRepo.countClassesByAcademicYearAndAgeRange(classRequest.getAcademicYear(), "4-5") >= maxClassLevel_2) {
                    throw new PermissionNotAcceptException("Số lớp 4-5 đã đạt tối đa");
                }
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel2());
                break;
            case "5-6":
                int maxClassLevel_3 = academicYearInformation.getTotalClassLevel3();
                if(classRepo.countClassesByAcademicYearAndAgeRange(classRequest.getAcademicYear(), "5-6") >= maxClassLevel_3) {
                    throw new PermissionNotAcceptException("Số lớp 5-6 đã đạt tối đa");
                }
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel3());
                break;
        }

        Classes savedClass = classRepo.save(newClass);
        classTeacherService.addTeacherIntoClass(savedClass.getId(), classRequest.getTeacherId());
        return savedClass;
    }

    public void createClassInThePast(AddClassRequest classRequest) {
        Classes newClass = new Classes();
        BeanUtils.copyProperties(classRequest, newClass);
        User manager = validateManager(classRequest.getManagerId());
        newClass.setManager(manager);
        newClass.setStatus(ClassStatusEnums.COMPLETED);
        validateCreatedBy(classRequest.getCreatedBy());

        SchoolYearInformation academicYearInformation = schoolYearInformationRepo.findByAcademicYear(classRequest.getAcademicYear());

        newClass.setCountChildrenRegisteredOnBoarding(0);
        newClass.setCountChildrenRegisteredTransport(0);
        switch (classRequest.getAgeRange()) {
            case "3-4":
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel1());
                break;
            case "4-5":
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel2());
                break;
            case "5-6":
                newClass.setTotalStudent(academicYearInformation.getTotalStudentLevel3());
                break;
        }

        Classes savedClass = classRepo.save(newClass);
        classTeacherService.addTeacherIntoClass(savedClass.getId(), classRequest.getTeacherId());
    }


    public User validateManager(String managerId) {
        User manager = userRepo.findById(managerId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người quản lý với id: " + managerId));

        if (!manager.getRole().equals(RoleEnums.CLASS_MANAGER)) {
            throw new PermissionNotAcceptException("Người này không có vai trò là Quản lý lớp (Class_Manager)");
        }

        return manager;
    }

    public void validateCreatedBy(String createdBy) {
        User createdByUser = userRepo.findById(createdBy)
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + createdBy));

        if (createdByUser.getRole() != RoleEnums.ADMIN) {
            throw new PermissionNotAcceptException("Cant create class with other role");
        }
    }

    public List<ClassListResponseWithNumberChildren> getClasses(String academicYear) {
        return classRepo.findClassesByFilters(academicYear);
    }

    public Classes getClassById(String id) {
        return classRepo.findById(id).get();
    }

    public List<TeacherOfClassResponse> getTeachersOfClass(String classId) {
        return classRepo.getTeacherOfClass(classId);
    }

    public List<ClassOption> getClassesByTeacherIdOrManagerId(String teacherId, String managerId) {
        List<Classes> classesList = classRepo.findClassesByTeacherIdOrManagerId(teacherId, managerId);
        return classesList.stream()
                .map(cls -> new ClassOption(cls.getId(), cls.getClassName()))
                .collect(Collectors.toList());
    }

    public List<ListAvailableClassOption> getAvailableClassOption(String academicYear) {
        return classRepo.listAvailableClassOptions(academicYear);
    }

    public List<ListAvailableClassOption> getAvailableClassOptionToTransfer(String academicYear, String childrenId) {
        return classRepo.listAvailableClassOptionsToTransfer(academicYear, childrenId);
    }


    public List<ClassListResponse> getClassWithManagerId(String id) {
        return classRepo.findByManagerId(id);
    }

    public List<ListClassWithStudyStatus> findAllByChildrenId(String childrenId) {
        return classRepo.findAllByChildrenId(childrenId);
    }
    public DailyReport report(String academicYear){
        return classRepo.countChildrenByAgeRange(academicYear);
    }

    @Transactional
    public void changeClassStatusToComplete(String classId) {
        Classes classes = classRepo.findById(classId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy lớp học với id: " + classId));
        classes.setStatus(ClassStatusEnums.COMPLETED);
        classRepo.save(classes);

        //find all children are studying of this class
        List<Children> listChildren = childrenRepo.findChildrenByClassId(classId);
        listChildren.stream().map(Children::getId).forEach(childrenId -> {
            childrenClassRepo.updateStatusByChildrenIdAndClassId(childrenId, classId, StudyStatusEnums.GRADUATED);
        });
    }
}
