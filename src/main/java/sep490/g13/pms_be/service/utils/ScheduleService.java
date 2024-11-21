package sep490.g13.pms_be.service.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.AttendanceLog;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.repository.AttendanceRepo;
import sep490.g13.pms_be.repository.ChildrenRepo;
import sep490.g13.pms_be.repository.ClassRepo;
import sep490.g13.pms_be.utils.enums.AttendanceStatusEnums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleService {

    private final AttendanceRepo attendanceRepository;
    private final ClassRepo classRepo;
    private final ChildrenRepo childrenRepository;

    public ScheduleService(AttendanceRepo attendanceRepository, ClassRepo classRepo, ChildrenRepo childrenRepository) {
        this.attendanceRepository = attendanceRepository;
        this.classRepo = classRepo;
        this.childrenRepository = childrenRepository;
    }
    @Scheduled(cron = "0 1 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void createDailyAttendanceLogs() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> classIds = classRepo.findClassIdsByAcademicYear("2024-2025");
        for (String classId : classIds) {
            createBaseLog(classId, today);
        }
    }

    public void createBaseLog(String classId, String today) {
        LocalDate providedDate = LocalDate.parse(today, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!attendanceRepository.existsByAttendanceDate(providedDate, classId) && providedDate.equals(LocalDate.now())) {
            Classes classes = classRepo.findById(classId)
                    .orElseThrow(() -> new DataNotFoundException("Class not found"));

            // Create attendance logs for each child in the class
            childrenRepository.findChildrenByClass(classId).forEach(children -> attendanceRepository.save(AttendanceLog.builder()
                    .children(children)
                    .attendanceDate(providedDate)
                    .status(AttendanceStatusEnums.ABSENT)
                    .classes(classes)
                    .build()));
        }
    }
}
