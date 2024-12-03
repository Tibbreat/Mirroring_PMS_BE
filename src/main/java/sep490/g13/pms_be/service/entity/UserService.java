package sep490.g13.pms_be.service.entity;


import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.Relationship;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.user.AddUserRequest;
import sep490.g13.pms_be.model.request.user.UpdateParentRequest;
import sep490.g13.pms_be.model.request.user.UpdateUserNameAndPasswordRequest;
import sep490.g13.pms_be.model.request.user.UpdateUserRequest;
import sep490.g13.pms_be.model.response.user.GetParentOptionResponse;
import sep490.g13.pms_be.model.response.user.GetUsersOptionResponse;
import sep490.g13.pms_be.repository.RelationshipRepo;
import sep490.g13.pms_be.repository.SchoolRepo;
import sep490.g13.pms_be.repository.UserRepo;
import sep490.g13.pms_be.service.utils.CloudinaryService;
import sep490.g13.pms_be.utils.StringUtils;
import sep490.g13.pms_be.utils.enums.RoleEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RelationshipRepo relationshipRepo;

    public User addUser(AddUserRequest request, MultipartFile image) {

        String accountName = StringUtils.generateUsername(request.getFullName());
        int count = userRepo.countByUsernameContaining(accountName);
        String username = count == 0 ? accountName : accountName + (count + 1);

        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setUsername(username.trim());
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode("pms@" + request.getIdCardNumber()));
        user.setEmail(username.trim() + "@pms.com");
        if (image != null && !image.isEmpty()) {
            String imagePath = cloudinaryService.saveImage(image);
            user.setImageLink(imagePath);
        }

        return userRepo.save(user);
    }




    public User findByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }

    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với id: " + id));
    }

    public Page<User> getAllByRole(List<String> roles, Boolean isActive, int size, int page, String fullName) {
        Pageable pageable = PageRequest.of(page, size);
        List<RoleEnums> roleEnums = new ArrayList<>();

        if (roles != null && !roles.isEmpty()) {
            for (String role : roles) {
                try {
                    RoleEnums roleEnum = RoleEnums.valueOf(role);
                    roleEnums.add(roleEnum);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Role không tồn tại: " + role, e);
                }
            }
        }

        return userRepo.getUsersByRoles(roleEnums, isActive, fullName, pageable); // Pass fullName
    }


    public void changeUserStatus(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với id: " + userId));
        Boolean newStatus = !user.getIsActive();
        int updatedRows = userRepo.updateUserStatus(userId, newStatus);
        if (updatedRows == 0) {
            throw new DataNotFoundException("Không tìm thấy người dùng với id: " + userId);
        }
    }

    public List<GetUsersOptionResponse> getUsers(String role) {
        try {
            RoleEnums roleEnum = RoleEnums.valueOf(role);
            return userRepo.findAllByRole(roleEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role không tồn tại: " + role, e);
        }
    }

    public List<GetUsersOptionResponse> getUserswithUserName(String role) {
        try {
            RoleEnums roleEnum = RoleEnums.valueOf(role);
            return userRepo.findAllByRoleWithUserName(roleEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role không tồn tại: " + role, e);
        }
    }

    public List<GetParentOptionResponse> getParentOptionsByTeacher(String teacherId) {

        try{
            return userRepo.getParentsByTeacher(teacherId);
        }catch(DataNotFoundException e){
            throw new DataNotFoundException("Không tìm thấy phụ huynh nào");
        }
    }

    public List<GetUsersOptionResponse> getTeacherAvailableInYear(String academicYear){
        return userRepo.getAvailableTeacherInAcademicYear(academicYear);
    }
    @Transactional
    public User updateUser(String id, UpdateUserRequest request, MultipartFile image) {
        // Find the user by username
        User user = userRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với id: " + id));

        // Update the user's information
        if (!request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());

            // Tạo username mới nếu fullName thay đổi
            String accountName = StringUtils.generateUsername(request.getFullName());
            int count = userRepo.countByUsernameContaining(accountName);
            String username = count == 0 ? accountName : accountName + (count + 1);

            user.setUsername(username);
            user.setEmail(username.trim() + "@pms.com");
        }
        user.setIdCardNumber(request.getIdCardNumber());
        user.setAddress(request.getAddress());
        user.setContractType(request.getContractType());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        if (image != null && !image.isEmpty()) {
            String imagePath = cloudinaryService.saveImage(image);
            user.setImageLink(imagePath);
        }

        // Save the updated user
        return userRepo.save(user);
    }

    public GetParentOptionResponse getParentByChildrenId(String childrenId) {
        return userRepo.findParentByChildrenId(childrenId);
    }

    @Transactional
    public void updateParentInfo(UpdateParentRequest request){
        User mother = userRepo.findByRelationship(request.getChildrenId(), "Mother");
        User father = userRepo.findByRelationship(request.getChildrenId(), "Father");
        mother.setFullName(request.getMother().getFullName());
        mother.setPhone(request.getMother().getPhone());
        father.setFullName(request.getFather().getFullName());
        father.setPhone(request.getFather().getPhone());
        userRepo.save(mother);
        userRepo.save(father);
    }
}
