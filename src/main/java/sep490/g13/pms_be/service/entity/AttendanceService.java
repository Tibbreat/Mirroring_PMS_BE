package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.AttendanceLog;
import sep490.g13.pms_be.model.request.attendance.Log;
import sep490.g13.pms_be.model.response.attendance.ChildrenAttendanceLog;
import sep490.g13.pms_be.repository.AttendanceRepo;
import sep490.g13.pms_be.service.utils.ScheduleService;
import sep490.g13.pms_be.utils.enums.AttendanceStatusEnums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepo attendanceRepository;

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private ScheduleService scheduleService;

    public List<ChildrenAttendanceLog> getBaseLog(String classId, String today) {
        scheduleService.createBaseLog(classId, today);
        return attendanceRepository.getChildrenAttendanceLog(classId, LocalDate.parse(today, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public List<ChildrenAttendanceLog> getChildrenAttendanceLogBaseOnVehicle(String managerId, String today) {
        LocalDate providedDate = LocalDate.parse(today, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return attendanceRepository.getChildrenAttendanceLogBaseOnVehicle(managerId, providedDate);
    }

    @Transactional
    public void updateAttendanceStatus(Log log) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate providedDate = LocalDate.parse(log.getAttendanceDate(), formatter);
        if (providedDate.equals(LocalDate.now())) {
            for (Log.ChildAttendance child : log.getChildren()) {
                AttendanceStatusEnums attendanceStatus = AttendanceStatusEnums.valueOf(child.getStatus().toUpperCase());
                attendanceRepository.updateAttendanceStatus(
                        child.getChildrenId(),
                        attendanceStatus,
                        providedDate,
                        log.getClassId(),
                        child.getNote());
            }
        } else {
            throw new IllegalArgumentException("Chỉ có thể cập nhật điểm danh cho ngày hôm nay");
        }
    }

    @Transactional
    public void updateAttendanceTime(String id, String type) {
        AttendanceLog attendanceLog = attendanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance log not found"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        switch (type) {
            case "1":
                attendanceLog.setMorningBoardingTime(calendar.getTime());
                break;
            case "2":
                attendanceLog.setMorningAlightingTime(calendar.getTime());
                break;
            case "3":
                attendanceLog.setAfternoonBoardingTime(calendar.getTime());
                break;
            case "4":
                attendanceLog.setAfternoonAlightingTime(calendar.getTime());
                break;
            default:
                throw new IllegalArgumentException("Invalid type specified");
        }

        // Lưu lại bản ghi đã được cập nhật
        attendanceRepo.save(attendanceLog);
    }
}
