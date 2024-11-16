package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.ClassTeacher;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.exception.other.PermissionNotAcceptException;
import sep490.g13.pms_be.model.response.teacher.TeacherOptionResponse;
import sep490.g13.pms_be.model.response.user.GetUsersOptionResponse;
import sep490.g13.pms_be.repository.ClassRepo;
import sep490.g13.pms_be.repository.ClassTeacherRepo;
import sep490.g13.pms_be.repository.UserRepo;
import sep490.g13.pms_be.utils.enums.RoleEnums;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClassTeacherRepo classTeacherRepo;
    @Autowired
    private ClassRepo classRepo;

    public List<TeacherOptionResponse> getTeachersForChildrenOfParent(String parentId) {
        User parent = userRepo.findById(parentId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy bố hoặc mẹ"));

        return userRepo.findTeachersByParentId(parentId);
    }
}

