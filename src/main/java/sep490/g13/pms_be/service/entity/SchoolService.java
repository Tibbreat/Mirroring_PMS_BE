package sep490.g13.pms_be.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.School;
import sep490.g13.pms_be.entities.SchoolYearInformation;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.school.AcademicInformationRequest;
import sep490.g13.pms_be.model.request.school.AddSchoolRequest;
import sep490.g13.pms_be.model.request.school.UpdateSchoolRequest;
import sep490.g13.pms_be.repository.AdmissionFileRepo;
import sep490.g13.pms_be.repository.SchoolRepo;
import sep490.g13.pms_be.repository.SchoolYearInformationRepo;
import sep490.g13.pms_be.repository.UserRepo;

@Service
public class SchoolService {
    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SchoolYearInformationRepo schoolYearInformationRepo;
    @Autowired
    private AdmissionFileRepo admissionFileRepo;

    public School saveSchool(AddSchoolRequest addSchoolRequest) {
        School school = new School();
        BeanUtils.copyProperties(addSchoolRequest, school);
        User principal = userRepo.findById(addSchoolRequest.getPrincipalId()).orElseThrow(() -> new RuntimeException("User not found"));
        school.setPrincipal(principal);
        return schoolRepo.save(school);
    }

    public School getSchools() {
        return schoolRepo.findFirstByOrderByIdAsc();
    }

    public SchoolYearInformation getAcademicYearInformation(String academicYear) {
        return schoolYearInformationRepo.findByAcademicYear(academicYear);
    }

    public School updateSchoolInformation(UpdateSchoolRequest request) {
        School school = schoolRepo.findById(request.getSchoolId()).orElseThrow(() -> new DataNotFoundException("School not found"));

        User principal = school.getPrincipal();
        principal.setFullName(request.getPrincipalName());
        principal.setPhone(request.getPrincipalPhone());
        userRepo.save(principal);

        school.setSchoolName(request.getSchoolName());
        school.setPhoneContact(request.getPhoneContact());
        school.setEmailContact(request.getEmailContact());
        school.setSchoolAddress(request.getAddress());
        return schoolRepo.save(school);
    }

    @Transactional
    public void updateAcademicInformation(AcademicInformationRequest request) {
        schoolYearInformationRepo.updateSchoolYearInformation(request);
        SchoolYearInformation savedData = schoolYearInformationRepo.findByAcademicYear(request.getAcademicYear());

        String schoolYearInformation = savedData.getId();
        if (request.getAdmissionFiles() != null) {
           admissionFileRepo.deleteBySchoolYearInformationId(schoolYearInformation);
            request.getAdmissionFiles().forEach(admissionFile -> {
                sep490.g13.pms_be.entities.AdmissionFile file = new sep490.g13.pms_be.entities.AdmissionFile();
                file.setFileName(admissionFile.getFileName());
                file.setNote(admissionFile.getNote());
                file.setSchoolYearInformationId(savedData);
                admissionFileRepo.save(file);
            });
        }
    }
}
