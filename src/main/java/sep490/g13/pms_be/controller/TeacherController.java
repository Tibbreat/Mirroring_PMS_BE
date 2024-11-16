package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sep490.g13.pms_be.model.response.teacher.TeacherOptionResponse;
import sep490.g13.pms_be.service.entity.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/pms/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @GetMapping("/by-parent/{parentId}")
    public ResponseEntity<List<TeacherOptionResponse>> getTeachersByParentId(@PathVariable String parentId) {
        List<TeacherOptionResponse> teachers = teacherService.getTeachersForChildrenOfParent(parentId);
        return ResponseEntity.ok(teachers);
    }
}
