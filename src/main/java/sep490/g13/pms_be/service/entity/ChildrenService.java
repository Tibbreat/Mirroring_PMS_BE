package sep490.g13.pms_be.service.entity;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.*;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.exception.other.OutRangeClassException;
import sep490.g13.pms_be.model.request.children.AddChildrenRequest;
import sep490.g13.pms_be.model.response.children.*;
import sep490.g13.pms_be.repository.*;
import sep490.g13.pms_be.service.utils.CloudinaryService;
import sep490.g13.pms_be.utils.ExcelUtils;
import sep490.g13.pms_be.utils.StringUtils;
import sep490.g13.pms_be.utils.enums.RoleEnums;
import sep490.g13.pms_be.utils.enums.StudyStatusEnums;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class ChildrenService {

    @Autowired
    private ChildrenRepo childrenRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private RelationshipRepo relationshipRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private ChildrenRouteRepo childrenRouteRepo;

    @Autowired
    private ChildrenClassRepo childrenClassRepo;
    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private StopLocationRepo stopLocationRepo;

    @Transactional
    public Children addChildren(AddChildrenRequest request, MultipartFile image) {
        Children children = Children.builder().childName(request.getChildName()).childBirthDate(request.getChildBirthDate()).childAddress(request.getChildAddress()).birthAddress(request.getBirthAddress()).gender(request.getGender()).religion(request.getReligion()).nationality(request.getNationality()).isRegisteredForBoarding(Boolean.FALSE).isRegisteredForTransport(Boolean.FALSE).build();
        children.setCreatedBy(request.getCreatedBy());

        if (image != null) {
            String imageUrl = cloudinaryService.saveImage(image);
            children.setImageUrl(imageUrl);
        }

        // Lưu đối tượng `Children` trước khi tạo `ChildrenClass`
        Children newChildren = childrenRepo.save(children);

        // Set class for children
        if (request.getClassId() != null) {
            Classes aClass = classRepo.findById(request.getClassId()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của lớp học"));

            int countChildren = childrenClassRepo.countChildrenByClassId(request.getClassId());
            if (countChildren >= aClass.getTotalStudent()) {
                throw new IllegalArgumentException("Lớp học đã đủ số lượng học sinh");
            }

            // Lưu `ChildrenClass` sau khi `Children` đã được lưu
            ChildrenClass cc = childrenClassRepo.save(ChildrenClass.builder().children(newChildren)  // Sử dụng `newChildren` đã được lưu
                    .classes(aClass).academicYear(aClass.getAcademicYear()).status(StudyStatusEnums.STUDYING).build());
            newChildren.setChildrenClasses(Set.of(cc));
        }

        User fatherExist = userRepo.existByIdCardNumber(request.getFather().getIdCardNumber());
        User motherExist = userRepo.existByIdCardNumber(request.getMother().getIdCardNumber());

        if (fatherExist != null || motherExist != null) {
            if (fatherExist != null) {
                relationshipRepo.save(Relationship.builder().childrenId(newChildren)  // Sử dụng `newChildren` đã được lưu
                        .parentId(fatherExist).relationship("Father").build());
            }

            if (motherExist != null) {
                relationshipRepo.save(Relationship.builder().childrenId(newChildren)  // Sử dụng `newChildren` đã được lưu
                        .parentId(motherExist).relationship("Mother").build());
            }
        } else {
            // For Father
            User father = userRepo.save(User.builder().username("parent." + StringUtils.generateUsername(request.getChildName()).toLowerCase()).password(passwordEncoder.encode("123456")).fullName(request.getFather().getFullName()).idCardNumber(request.getFather().getIdCardNumber()).phone(request.getFather().getPhone()).isActive(Boolean.TRUE).role(RoleEnums.PARENT).build());

            relationshipRepo.save(Relationship.builder().childrenId(newChildren)  // Sử dụng `newChildren` đã được lưu
                    .parentId(father).relationship("Father").build());

            // For Mother
            User mother = userRepo.save(User.builder().fullName(request.getMother().getFullName()).idCardNumber(request.getMother().getIdCardNumber()).phone(request.getMother().getPhone()).isActive(Boolean.TRUE).role(RoleEnums.PARENT).build());

            relationshipRepo.save(Relationship.builder().childrenId(newChildren)  // Sử dụng `newChildren` đã được lưu
                    .parentId(mother).relationship("Mother").build());
        }

        return newChildren;
    }


    public Page<ChildrenListResponse> findChildrenByFilter(String academicYear, String childName, int page, int size) {
        return childrenRepo.findChildrenByFilter(academicYear, childName, PageRequest.of(page, size));
    }

    public Page<ChildrenListResponse> findChildrenByClass(String classId, int page, int size) {
        return childrenRepo.findChildrenByClass(classId, PageRequest.of(page, size));
    }

    public ChildrenDetailResponse getChildrenDetail(String childrenId) {
        return childrenRepo.findChildrenDetailById(childrenId);
    }

    @Transactional
    public void transferClass(String childrenId, String oldClassId, String newClassId) {
        Children children = childrenRepo.findById(childrenId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của trẻ"));
        Classes oldClass = classRepo.findById(oldClassId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của lớp học"));
        Classes newClass = classRepo.findById(newClassId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của lớp học"));

        // Check if the new class has enough slots
        int countChildren = childrenClassRepo.countChildrenByClassId(newClassId);
        if (countChildren >= newClass.getTotalStudent()) {
            throw new IllegalArgumentException("Lớp học đã đủ số lượng học sinh");
        }

        // Update children class
        childrenClassRepo.updateStatusByChildrenIdAndClassId(childrenId, oldClassId, StudyStatusEnums.MOVED_OUT);

        //Add new class for children
        childrenClassRepo.save(ChildrenClass.builder()
                .children(children)
                .classes(newClass)
                .academicYear(newClass.getAcademicYear())
                .status(StudyStatusEnums.STUDYING)
                .build());

        // Update count children registered transport
        if (children.getIsRegisteredForTransport()) {
            classRepo.updateCountStudentRegisteredTransport(oldClassId, oldClass.getCountChildrenRegisteredTransport() - 1);
            classRepo.updateCountStudentRegisteredTransport(newClassId, newClass.getCountChildrenRegisteredTransport() + 1);
        }
        if (children.getIsRegisteredForBoarding()) {
            classRepo.updateCountStudentRegisteredOnBoarding(oldClassId, oldClass.getCountChildrenRegisteredOnBoarding() - 1);
            classRepo.updateCountStudentRegisteredOnBoarding(newClassId, newClass.getCountChildrenRegisteredOnBoarding() + 1);
        }
    }

    @Transactional
    public void updateServiceStatus(String childrenId, String service, String routeId, String stopLocation) {
        Children children = childrenRepo.findById(childrenId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của trẻ"));
        Classes classes = childrenClassRepo.findClassByChildrenId(childrenId);
        if (classes == null) {
            throw new DataNotFoundException("Không tìm thấy dữ liệu của lớp học cho trẻ");
        }

        switch (service) {
            case "transport":
                if (children.getIsRegisteredForTransport()) {
                    // Hủy đăng ký đưa đón
                    Vehicle currentVehicle = vehicleRepo.findById(children.getVehicle().getId())
                            .orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của xe"));
                    childrenRouteRepo.deleteByChildrenId(childrenId);
                    children.setVehicle(null);
                    children.setRegisteredStopLocation(null);
                    children.setIsRegisteredForTransport(Boolean.FALSE);
                    childrenRepo.save(children);

                    int updatedCount = currentVehicle.getNumberChildrenRegistered() - 1;
                    vehicleRepo.updateNumberChildrenRegistered(currentVehicle.getId(), updatedCount);
                    classes.setCountChildrenRegisteredTransport(classes.getCountChildrenRegisteredTransport() - 1);
                    classRepo.save(classes);
                } else {
                    // Đăng ký đưa đón
                    Route route = routeRepo.findById(routeId)
                            .orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của tuyến đường"));

                    StopLocation sl = stopLocationRepo.findById(stopLocation)
                            .orElseThrow(() -> new DataNotFoundException("Không tìm thấy điểm dừng"));

                    // Tìm danh sách xe theo tuyến, sắp xếp theo số chỗ giảm dần
                    List<Vehicle> vehicles = vehicleRepo.findAllByRouteIdOrderByNumberOfSeatsDesc(routeId);

                    // Thuật toán Best Fit: Tìm xe có số trẻ đăng ký gần với 90% số chỗ tối đa
                    Vehicle bestFitVehicle = getVehicle(vehicles);

                    // Xếp xe cho trẻ
                    children.setVehicle(bestFitVehicle);
                    children.setRegisteredStopLocation(sl);
                    children.setIsRegisteredForTransport(Boolean.TRUE);
                    childrenRepo.save(children);

                    // Cập nhật số trẻ đăng ký trên xe
                    int updatedCount = bestFitVehicle.getNumberChildrenRegistered() + 1;
                    vehicleRepo.updateNumberChildrenRegistered(bestFitVehicle.getId(), updatedCount);

                    // Lưu thông tin tuyến đường của trẻ
                    childrenRouteRepo.save(ChildrenRoute.builder().children(children).route(route).build());

                    // Cập nhật số trẻ đăng ký đưa đón trong lớp
                    classes.setCountChildrenRegisteredTransport(classes.getCountChildrenRegisteredTransport() + 1);
                    classRepo.save(classes);
                }
                break;

            case "boarding":
                boolean newBoardingStatus = !children.getIsRegisteredForBoarding();
                childrenRepo.updateBoardingServiceStatus(childrenId, newBoardingStatus);

                if (newBoardingStatus) {
                    classes.setCountChildrenRegisteredOnBoarding(classes.getCountChildrenRegisteredOnBoarding() + 1);
                } else {
                    classes.setCountChildrenRegisteredOnBoarding(classes.getCountChildrenRegisteredOnBoarding() - 1);
                }
                classRepo.save(classes);
                break;

            default:
                throw new IllegalArgumentException("Invalid service type: " + service);
        }
    }

    private Vehicle getVehicle(List<Vehicle> vehicles) {
        Vehicle bestFitVehicle = null;
        int minSeatsDifference = Integer.MAX_VALUE;

        for (Vehicle vehicle : vehicles) {
            int maxAllowedChildren = (int) (0.9 * vehicle.getNumberOfSeats());
            if (vehicle.getNumberChildrenRegistered() <= maxAllowedChildren) {
                int seatsDifference = maxAllowedChildren - vehicle.getNumberChildrenRegistered();
                if (seatsDifference < minSeatsDifference) {
                    minSeatsDifference = seatsDifference;
                    bestFitVehicle = vehicle;
                }
            }
        }
        if (bestFitVehicle == null) {
            throw new DataNotFoundException("Không có xe phù hợp cho tuyến này");
        }
        return bestFitVehicle;
    }


    public List<ChildrenListByRoute> getChildrenByRouteId(String routeId) {
        return childrenRepo.findChildrenByRouteId(routeId);
    }

    public void generateExcel(HttpServletResponse response, String classId) throws IOException {
        Classes classes = classRepo.findById(classId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của lớp học"));
        List<ChildrenDataSheetRow> childrenList = childrenRepo.getChildrenDataSheet(classId);

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Danh sách lớp " + classes.getClassName() + " năm học " + classes.getAcademicYear());
            HSSFCellStyle headerStyle = createHeaderStyle(workbook);
            HSSFCellStyle dataStyle = createDataStyle(workbook);
            HSSFCellStyle dateStyle = createDateStyle(workbook, dataStyle);

            // Create header row and set cell values
            String[] headers = {"STT", "Họ và tên", "Ngày sinh", "Lớp", "Quốc tịch", "Tôn giáo", "Địa chỉ nơi sinh", "Địa chỉ hiện tại", "Giới tính", "Tên cha", "Tên mẹ"};
            createHeaderRow(sheet, headers, headerStyle);

            // Populate data rows
            for (int i = 0; i < childrenList.size(); i++) {
                ChildrenDataSheetRow child = childrenList.get(i);
                HSSFRow dataRow = sheet.createRow(i + 1);
                populateDataRow(dataRow, i + 1, child, classes.getClassName(), dataStyle, dateStyle);
            }

            autoSizeColumns(sheet, headers.length);
            writeWorkbookToResponse(workbook, response);
        }
    }

    public void generateExcelByAcademicYear(HttpServletResponse response, String academicYear) throws IOException {
        List<ChildrenDataSheetByAcademic> childrenList = childrenRepo.getChildrenDataSheetByAcademicYear(academicYear);

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Danh sách trẻ năm học " + academicYear);
            HSSFCellStyle headerStyle = createHeaderStyle(workbook);
            HSSFCellStyle dataStyle = createDataStyle(workbook);
            HSSFCellStyle dateStyle = createDateStyle(workbook, dataStyle);

            // Create header row and add "Tên lớp" column
            String[] headers = {"STT", "Họ và tên", "Ngày sinh", "Giới tính", "Quốc tịch", "Tôn giáo", "Địa chỉ nơi sinh", "Địa chỉ hiện tại", "Tên cha", "Tên mẹ", "Tên lớp"};
            createHeaderRow(sheet, headers, headerStyle);

            // Populate data rows with class name column
            for (int i = 0; i < childrenList.size(); i++) {
                ChildrenDataSheetByAcademic child = childrenList.get(i);
                HSSFRow dataRow = sheet.createRow(i + 1);
                populateAcademicYearDataRow(dataRow, i + 1, child, dataStyle, dateStyle);
            }

            autoSizeColumns(sheet, headers.length);
            writeWorkbookToResponse(workbook, response);
        }
    }

    public void generateExcelByVehicle(HttpServletResponse response, String vehicleId) throws IOException {
        Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của xe"));
        List<ChildrenDataSheetByVehicle> childrenList = childrenRepo.getChildrenDataSheetByVehicle(vehicleId);

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Danh sách trẻ đăng ký " + vehicle.getVehicleName());
            HSSFCellStyle headerStyle = createHeaderStyle(workbook);
            HSSFCellStyle dataStyle = createDataStyle(workbook);
            HSSFCellStyle dateStyle = createDateStyle(workbook, dataStyle);

            String[] headers = {"STT", "Họ và tên", "Giới tính", "Điểm đón", "Lớp", "Lên xe", "Xuống xe", "Ghi chú"};
            createHeaderRow(sheet, headers, headerStyle);
            for (int i = 0; i < childrenList.size(); i++) {
                ChildrenDataSheetByVehicle child = childrenList.get(i);
                HSSFRow dataRow = sheet.createRow(i + 1);
                populateVehicleDataRow(dataRow, i + 1, child, dataStyle, dateStyle);
            }
            autoSizeColumns(sheet, headers.length);
            writeWorkbookToResponse(workbook, response);
        }

    }

    // Helper method to create header style
    private HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor customColor = palette.findSimilarColor(115, 192, 164);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(customColor.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(headerStyle);
        return headerStyle;
    }

    // Helper method to create data style with borders
    private HSSFCellStyle createDataStyle(HSSFWorkbook workbook) {
        HSSFCellStyle dataStyle = workbook.createCellStyle();
        setBorders(dataStyle);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return dataStyle;
    }

    // Helper method to create date style based on data style
    private HSSFCellStyle createDateStyle(HSSFWorkbook workbook, HSSFCellStyle baseStyle) {
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(baseStyle);
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));
        return dateStyle;
    }

    // Helper method to set borders
    private void setBorders(HSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // Helper method to create header row
    private void createHeaderRow(HSSFSheet sheet, String[] headers, HSSFCellStyle headerStyle) {
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // Helper method to populate data row for children list
    private void populateDataRow(HSSFRow row, int index, ChildrenDataSheetRow child, String className, HSSFCellStyle dataStyle, HSSFCellStyle dateStyle) {
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(child.getChildName());
        row.createCell(2).setCellValue(child.getChildBirthDate());
        row.createCell(3).setCellValue(className);
        row.createCell(4).setCellValue(child.getNationality());
        row.createCell(5).setCellValue(child.getReligion());
        row.createCell(6).setCellValue(child.getBirthAddress());
        row.createCell(7).setCellValue(child.getChildAddress());
        row.createCell(8).setCellValue("male".equalsIgnoreCase(child.getGender()) ? "Nam" : "Nữ");
        row.createCell(9).setCellValue(child.getFatherName());
        row.createCell(10).setCellValue(child.getMotherName());
        applyStyle(row, dataStyle, dateStyle, 2); // Apply date style to birth date column
    }

    // Helper method to populate data row for academic year list
    private void populateAcademicYearDataRow(HSSFRow row, int index, ChildrenDataSheetByAcademic child, HSSFCellStyle dataStyle, HSSFCellStyle dateStyle) {
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(child.getChildName());
        row.createCell(2).setCellValue(child.getChildBirthDate());
        row.createCell(3).setCellValue("male".equalsIgnoreCase(child.getGender()) ? "Nam" : "Nữ");
        row.createCell(4).setCellValue(child.getNationality());
        row.createCell(5).setCellValue(child.getReligion());
        row.createCell(6).setCellValue(child.getBirthAddress());
        row.createCell(7).setCellValue(child.getChildAddress());
        row.createCell(8).setCellValue(child.getFatherName());
        row.createCell(9).setCellValue(child.getMotherName());
        row.createCell(10).setCellValue(child.getClassName());
        applyStyle(row, dataStyle, dateStyle, 2);
    }

    // Helper method to populate data row for vehicle list
    private void populateVehicleDataRow(HSSFRow row, int index, ChildrenDataSheetByVehicle child, HSSFCellStyle dataStyle, HSSFCellStyle dateStyle) {
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(child.getChildName());
        row.createCell(2).setCellValue("male".equalsIgnoreCase(child.getChildrenGender()) ? "Nam" : "Nữ");
        row.createCell(3).setCellValue(child.getStopLocationRegistered());
        row.createCell(4).setCellValue(child.getChildrenClassName());
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue("");
        row.createCell(7).setCellValue("");
        applyStyle(row, dataStyle, dateStyle, 10);
    }

    // Helper method to apply cell style
    private void applyStyle(HSSFRow row, HSSFCellStyle dataStyle, HSSFCellStyle dateStyle, int dateColumnIndex) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            row.getCell(i).setCellStyle(i == dateColumnIndex ? dateStyle : dataStyle);
        }
    }

    // Helper method to auto-size columns
    private void autoSizeColumns(HSSFSheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // Helper method to write workbook to response output stream
    private void writeWorkbookToResponse(HSSFWorkbook workbook, HttpServletResponse response) throws IOException {
        try (ServletOutputStream ops = response.getOutputStream()) {
            workbook.write(ops);
        }
    }


    public List<AddChildrenRequest> convertChildrenDataFromExcel(MultipartFile file) {
        if (ExcelUtils.isValidExcelFile(file)) {
            try {
                return ExcelUtils.getChildrenDataFromExcel(file.getInputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
        throw new IllegalArgumentException("The file is not a valid excel file");
    }

    @Transactional
    public void saveChildrenDataFromExcel(List<AddChildrenRequest> data) {
        String classId = data.get(0).getClassId();
        Classes aClass = classRepo.findById(classId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của lớp học"));

        // Check if the data size exceeds the total student limit
        if (aClass.getTotalStudent() < data.size()) {
            throw new OutRangeClassException("Số lượng học sinh vượt quá số lượng tối đa của lớp học (" + aClass.getTotalStudent() + " học sinh)");
        }

        // Calculate available slots
        int availableSlot = aClass.getTotalStudent() - childrenClassRepo.countChildrenByClassId(classId);
        if (data.size() > availableSlot) {
            throw new OutRangeClassException("Lớp chỉ còn " + availableSlot + " chỗ trống");
        }

        // Get the current count of disabled children in the class
        long existingDisabledChildrenCount = childrenClassRepo.countDisabledChildrenByClassId(classId);

        // Count disabled children in the new data
        long newDisabledChildrenCount = data.stream().filter(AddChildrenRequest::getIsDisabled).count();

        // Validate that adding new disabled children does not exceed the limit of 2
        if (existingDisabledChildrenCount + newDisabledChildrenCount > 2) {
            throw new OutRangeClassException("Lớp đã có 2 hoặc nhiều hơn 2 trẻ bị khuyết tật. Không thể thêm trẻ.");
        }

        // Save each child if all checks pass
        data.forEach(children -> addChildren(children, null));
    }

    public void uploadImage(String childrenId, MultipartFile image) {
        Children children = childrenRepo.findById(childrenId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dữ liệu của trẻ"));
        String imageUrl = cloudinaryService.saveImage(image);
        children.setImageUrl(imageUrl);
        childrenRepo.save(children);
    }

    public List<ChildrenOptionResponse> getChildrenByTeacherId(String teacherId) {
        userRepo.findById(teacherId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giáo viên"));
        return childrenRepo.findChildrenByTeacherId(teacherId);
    }
}
