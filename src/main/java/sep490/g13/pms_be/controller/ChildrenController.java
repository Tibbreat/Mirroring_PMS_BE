package sep490.g13.pms_be.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.*;
import sep490.g13.pms_be.model.request.children.AddChildrenRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.model.response.children.*;
import sep490.g13.pms_be.service.entity.ChildrenService;
import sep490.g13.pms_be.utils.ValidationUtils;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pms/children")
public class ChildrenController {
    @Autowired
    private ChildrenService childrenService;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private <T> ResponseEntity<PagedResponseModel<T>> buildPagedResponse(Page<T> results, int page) {
        List<T> contentList = results.getContent();
        String message = contentList.isEmpty() ? "No data found" : "Found " + results.getTotalElements() + " items";
        return ResponseEntity.ok(
                PagedResponseModel.<T>builder()
                        .page(page)
                        .size(DEFAULT_PAGE_SIZE)
                        .msg(message)
                        .total(results.getTotalElements())
                        .listData(contentList)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponseModel<ChildrenListResponse>> getChildren(
            @RequestParam(value = "academicYear", required = false) String academicYear,
            @RequestParam(value = "childName", required = false) String childName,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Page<ChildrenListResponse> results = childrenService.findChildrenByFilter(academicYear, childName, page - 1, DEFAULT_PAGE_SIZE);
        return buildPagedResponse(results, page);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<ChildrenListByClass>> getChildrenByClass(@PathVariable String classId) {
        return ResponseEntity.ok(childrenService.findChildrenByClass(classId));
    }

    @PostMapping("/new-children")
    public ResponseEntity<ResponseModel<?>> addChildren(
            @RequestPart("children") @Valid AddChildrenRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.<String>builder()
                            .message("Add children failed")
                            .data(ValidationUtils.getValidationErrors(bindingResult))
                            .build());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseModel.<Children>builder()
                        .message("Add children successfully")
                        .data(childrenService.addChildren(request, image))
                        .build());
    }

    @GetMapping("/{childrenId}")
    public ResponseEntity<ResponseModel<ChildrenDetailResponse>> getChildrenDetail(@PathVariable String childrenId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<ChildrenDetailResponse>builder()
                        .message("Get children detail successfully")
                        .data(childrenService.getChildrenDetail(childrenId))
                        .build());
    }

    @PutMapping("/service/{childrenId}/{service}")
    public ResponseEntity<ResponseModel<?>> updateServiceStatus(@PathVariable String childrenId,
                                                                @PathVariable String service,
                                                                @RequestParam(required = false) String routeId,
                                                                @RequestParam(required = false) String stopLocation) {
        childrenService.updateServiceStatus(childrenId, service, routeId, stopLocation);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<String>builder()
                        .message("Update service status successfully")
                        .data("Update " + service + " service status successfully")
                        .build());
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<ChildrenListByRoute>> getChildrenByRoute(@PathVariable(value = "routeId") String routeId) {
        List<ChildrenListByRoute> results = childrenService.getChildrenByRouteId(routeId);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @GetMapping("/excel/{classId}")
    public ResponseEntity<String> exportChildrenToExcel(@PathVariable String classId, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=danh_sach_lop.xls";
        response.setHeader(headerKey, headerValue);
        childrenService.generateExcel(response, classId);
        response.flushBuffer();
        return ResponseEntity.ok("Export excel successfully");
    }

    @GetMapping("/excel/academic-year/{academicYear}")
    public ResponseEntity<String> exportChildrenByAcademicYearToExcel(@PathVariable String academicYear, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=ChildrenData_" + academicYear + ".xls";
        response.setHeader(headerKey, headerValue);

        childrenService.generateExcelByAcademicYear(response, academicYear);
        response.flushBuffer();
        return ResponseEntity.ok("Export successful");
    }

    @GetMapping("/excel/vehicle/{vehicleId}")
    public ResponseEntity<String> exportChildrenByVehicleId(@PathVariable String vehicleId, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=ChildrenData.xls";
        response.setHeader(headerKey, headerValue);

        childrenService.generateExcelByVehicle(response, vehicleId);
        response.flushBuffer();
        return ResponseEntity.ok("Export successful");
    }

    @PostMapping("/excel/import")
    public ResponseEntity<List<AddChildrenRequest>> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        return ResponseEntity
                .ok(childrenService.convertChildrenDataFromExcel(file));
    }

    @PostMapping("/excel/import/save")
    public ResponseEntity<ResponseModel<?>> saveChildrenData(@RequestBody List<AddChildrenRequest> childrenData) {
        childrenService.saveChildrenDataFromExcel(childrenData);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseModel.<String>builder()
                        .message("Save children data successfully")
                        .data("Save children data successfully")
                        .build());
    }

    @PutMapping("/upload-image/{childrenId}")
    public ResponseEntity<ResponseModel<?>> uploadImage(@PathVariable String childrenId,
                                                        @RequestPart("image") MultipartFile image) {
        childrenService.uploadImage(childrenId, image);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<String>builder()
                        .message("Upload image successfully")
                        .data("Upload image successfully")
                        .build());
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<ChildrenOptionResponse>> getChildrenByTeacherId(@PathVariable String teacherId) {
        List<ChildrenOptionResponse> childrenList = childrenService.getChildrenByTeacherId(teacherId);
        return ResponseEntity.ok(childrenList);
    }

    @PutMapping("/transfer-class")
    public ResponseEntity<ResponseModel<?>> transferClass(@RequestBody TransferClassRequest request) {
        System.out.println(request.toString());
        childrenService.transferClass(request.getChildrenId(), request.getOldClassId(), request.getNewClassId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<String>builder()
                        .message("Transfer class successfully")
                        .data("Transfer class successfully")
                        .build());
    }
}
