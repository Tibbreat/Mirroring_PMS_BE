package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sep490.g13.pms_be.entities.SchoolYearInformation;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.repository.ClassRepo;
import sep490.g13.pms_be.service.entity.SchoolService;

import java.util.List;

@RestController
@RequestMapping("/pms/public")
public class PublicAPIController {
    @Autowired
    private ClassRepo classRepo;
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/academic-year")
    public ResponseEntity<ResponseModel<?>> getAcademicYears() {
        List<String> academicYears = classRepo.listAllAcademicYears();
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .message("Danh sách các năm học")
                        .data(academicYears)
                        .build()
        );
    }
    @GetMapping("/school/academic-year")
    public ResponseEntity<ResponseModel<?>> getAcademicYearInformation(@RequestParam String academicYear) {
        return ResponseEntity.ok(ResponseModel.<SchoolYearInformation>builder()
                .data(schoolService.getAcademicYearInformation(academicYear))
                .message("Academic year information fetched successfully")
                .build());
    }

}
