package sep490.g13.pms_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.model.request.user.AddUserRequest;
import sep490.g13.pms_be.model.request.user.UpdateParentRequest;
import sep490.g13.pms_be.model.request.user.UpdateUserRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.model.response.user.GetParentOptionResponse;
import sep490.g13.pms_be.model.response.user.GetUsersOptionResponse;
import sep490.g13.pms_be.repository.UserRepo;
import sep490.g13.pms_be.service.entity.UserService;
import sep490.g13.pms_be.utils.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping("/pms/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;


    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<?>> addUser(
            @RequestPart("user") @Valid AddUserRequest addUserRequest,
            @RequestPart(value = "image", required = false) MultipartFile image,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationUtils.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest()
                    .body(ResponseModel.<String>builder()
                            .message("Thêm người dùng không thành công")
                            .data(errorMessage)
                            .build());
        }
        User newUser = userService.addUser(addUserRequest, image); // Pass the image to the service

        String message = newUser == null ? "Thêm người dùng không thành công" : "Thêm người dùng thành công";
        HttpStatus responseCode = newUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

        return ResponseEntity.status(responseCode)
                .body(ResponseModel.<User>builder()
                        .message(message)
                        .data(newUser)
                        .build());
    }


    @GetMapping("/school")
    public ResponseEntity<PagedResponseModel<User>> getUsers(
            @RequestParam int page,
            @RequestParam(required = false) List<String> role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String fullName) {

        int size = 10;
        Page<User> results = userService.getAllByRole(role, isActive, size, page - 1, fullName);
        List<User> users = results.getContent();

        String msg = users.isEmpty() ? "Không có dữ liệu" : "Tìm thấy " + results.getTotalElements();

        PagedResponseModel<User> pagedResponse = PagedResponseModel.<User>builder()
                .page(page)
                .size(size)
                .msg(msg)
                .total(results.getTotalElements())
                .listData(users)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseModel<?>> getUser(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<User>builder()
                        .message("Tìm thấy người dùng có ID: " + userId)
                        .data(userService.getUserById(userId))
                        .build());

    }


    @PutMapping("/user/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<?>> changeUserStatus(@PathVariable String userId) {
        userService.changeUserStatus(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<String>builder()
                        .message("User status updated successfully")
                        .build());
    }

    @GetMapping("/options")
    public ResponseEntity<ResponseModel<?>> getUsersOption(
            @RequestParam String role
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<List<GetUsersOptionResponse>>builder()
                        .message("Get users option successfully")
                        .data(userService.getUsers(role))
                        .build());
    }

    @GetMapping("/option-username")
    public ResponseEntity<ResponseModel<?>> getUsersOptionWithUserName(
            @RequestParam String role) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<List<GetUsersOptionResponse>>builder()
                        .message("Get users option successfully")
                        .data(userService.getUserswithUserName(role))
                        .build());
    }
    @GetMapping("/parents-by-teacher/{teacherId}")
    public ResponseEntity<ResponseModel<?>> getParentsByTeacher(@PathVariable("teacherId") String teacherId) {
        List<GetParentOptionResponse> parents = userService.getParentOptionsByTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<List<GetParentOptionResponse>>builder()
                        .message("Get users option successfully")
                        .data(parents)
                        .build());
    }

    @GetMapping("/options/available-teacher/{academicYear}")
    public ResponseEntity<ResponseModel<?>> getTeacherAvailableInYear(@PathVariable String academicYear ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<List<GetUsersOptionResponse>>builder()
                        .message("Get users option successfully")
                        .data(userService.getTeacherAvailableInYear(academicYear))
                        .build());
    }

    @GetMapping("/options/available-transport-manager")
    public ResponseEntity<ResponseModel<?>> getTransportManagerAvailable() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<List<GetUsersOptionResponse>>builder()
                        .message("Get users option successfully")
                        .data(userRepo.findTransportManagerAvailable())
                        .build());
    }
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<ResponseModel<?>> updateUser(@PathVariable String userId,
                                                       @RequestPart("user") UpdateUserRequest request,
                                                       @RequestPart(value = "image", required = false) MultipartFile image,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationUtils.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest()
                    .body(ResponseModel.<String>builder()
                            .message("Thay đổi người dùng không thành công")
                            .data(errorMessage)
                            .build());
        }
        User newUser = userService.updateUser(userId,request, image); // Pass the image to the service

        String message = newUser == null ? "Thay đổi người dùng không thành công" : "Thay đổi người dùng thành công";
        HttpStatus responseCode = newUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;

        return ResponseEntity.status(responseCode)
                .body(ResponseModel.<User>builder()
                        .message(message)
                        .data(newUser)
                        .build());
    }

    @GetMapping("/parent/{childrenId}")
    public ResponseEntity<ResponseModel<?>> getParentByChildrenId(@PathVariable String childrenId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseModel.<GetParentOptionResponse>builder()
                        .message("Tìm thấy phụ huynh")
                        .data(userService.getParentByChildrenId(childrenId))
                        .build());
    }

    @PutMapping("/updateParent")
    @PreAuthorize("hasRole('ADMIN' or 'CLASS_MANAGER')")
    public ResponseEntity<?> updateParent(@RequestBody UpdateParentRequest request) {
        userService.updateParentInfo(request);
        return ResponseEntity.ok().body("Parent information updated successfully.");
    }
}
