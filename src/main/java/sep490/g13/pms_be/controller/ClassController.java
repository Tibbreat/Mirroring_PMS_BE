package sep490.g13.pms_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.model.request.classes.AddClassRequest;
import sep490.g13.pms_be.model.response.classes.ClassListResponse;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.model.response.classes.ClassListResponseWithNumberChildren;
import sep490.g13.pms_be.model.response.classes.ClassOption;
import sep490.g13.pms_be.model.response.classes.ListClassWithStudyStatus;
import sep490.g13.pms_be.model.response.kitchen.report.DailyReport;
import sep490.g13.pms_be.model.response.user.TeacherOfClassResponse;
import sep490.g13.pms_be.service.entity.*;
import sep490.g13.pms_be.utils.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping("/pms/classes")
public class ClassController {
    @Autowired
    private ClassService classService;

    @Autowired
    private ClassTeacherService classTeacherService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel<?>> addNewClass(
            @RequestBody @Valid AddClassRequest classRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(
                    ResponseModel.builder()
                            .message("Thông tin lớp học không hợp lệ")
                            .data(validationErrors)
                            .build()
            );
        }
        Classes savedClass = classService.createNewClass(classRequest);
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .message("Thêm lớp học thành công")
                        .data(savedClass)
                        .build()
        );

    }

    @GetMapping()
    public ResponseEntity<List<ClassListResponseWithNumberChildren>> getClasses(@RequestParam String academicYear) {
        List<ClassListResponseWithNumberChildren> classList = classService.getClasses(academicYear);
        return ResponseEntity.status(HttpStatus.OK).body(classList);
    }

    @GetMapping("/class/teacher/{teacherId}")
    public ResponseEntity<List<Classes>> getClassByTeacherId(@PathVariable String teacherId) {
        List<Classes> classes = classTeacherService.getClassByTeacherId(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(classes);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ResponseModel<?>> getClassById(@PathVariable String classId) {
        Classes classes = classService.getClassById(classId);
        return ResponseEntity.ok(ResponseModel.<Classes>builder()
                .message("Lấy thông tin lớp học thành công")
                .data(classes)
                .build());
    }

    @GetMapping("/teacher/class/{classId}")
    public ResponseEntity<ResponseModel<?>> getTeacherOfClass(@PathVariable String classId) {
        List<TeacherOfClassResponse> teachers = classService.getTeachersOfClass(classId);
        return ResponseEntity.ok(ResponseModel.<List<TeacherOfClassResponse>>builder()
                .message("Lấy thông tin giáo viên của lớp học thành công")
                .data(teachers)
                .build());
    }

    @GetMapping("/class-option")
    public ResponseEntity<ResponseModel<?>> getClassesByTeacherOrManagerId(
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String managerId) {
        List<ClassOption> classes = classService.getClassesByTeacherIdOrManagerId(teacherId, managerId);
        return ResponseEntity.ok(ResponseModel.<List<ClassOption>>builder()
                .message("Lấy danh sách lớp thành công")
                .data(classes)
                .build());
    }

    @GetMapping("/available/{academicYear}")
    public ResponseEntity<?> getAvailableClassOption(@PathVariable String academicYear) {
        return ResponseEntity.ok(classService.getAvailableClassOption(academicYear));
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<ResponseModel<?>> getClassByManagerId(@PathVariable String managerId) {
        return ResponseEntity.ok(ResponseModel.<List<ClassListResponse>>builder()
                .message("Lấy danh sách lớp học của quản lý")
                .data(classService.getClassWithManagerId(managerId))
                .build());
    }


    @GetMapping("/byTeacher/{teacherId}/schoolYear/{schoolYear}")
    public ResponseEntity<List<ClassOption>> getClassesByTeacherAndSchoolYear(
            @PathVariable String teacherId,
            @PathVariable String schoolYear) {
        List<ClassOption> classOptions = classTeacherService.getClassesByTeacherAndSchoolYear(teacherId, schoolYear);
        if (!classOptions.isEmpty()) {
            return ResponseEntity.ok(classOptions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/byChildren/{childrenId}")
    public ResponseEntity<List<ListClassWithStudyStatus>> getClassesByChildrenId(@PathVariable String childrenId) {
        List<ListClassWithStudyStatus> classOptions = classService.findAllByChildrenId(childrenId);
        if (!classOptions.isEmpty()) {
            return ResponseEntity.ok(classOptions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/transfer-options/academic-year/{academicYear}/children/{childrenId}")
    public ResponseEntity<?> getTransferOptions(@PathVariable String academicYear, @PathVariable String childrenId) {
        return ResponseEntity.ok(classService.getAvailableClassOptionToTransfer(academicYear, childrenId));
    }

    @GetMapping("/kitchen/report/{academicYear}")
    public ResponseEntity<DailyReport> getReport(@PathVariable String academicYear) {
        return ResponseEntity.ok(classService.report(academicYear));
    }

    @PutMapping("/change-class-status/{classId}")
    public ResponseEntity<ResponseModel<?>> changeClassDescription(@PathVariable String classId) {
        classService.changeClassStatusToComplete(classId);
        return ResponseEntity.ok(ResponseModel.builder()
                .message("Thay đổi trạng thái lớp học thành công")
                .build());
    }
}
