package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.model.request.attendance.Log;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.service.entity.AttendanceService;
import sep490.g13.pms_be.service.utils.ScheduleService;

@RestController
@RequestMapping("/pms/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{classId}")
    public ResponseEntity<ResponseModel<?>> getChildrenAttendanceLog(@PathVariable String classId, @RequestParam String today) {
        return ResponseEntity.ok(ResponseModel.builder()
                .data(attendanceService.getBaseLog(classId, today))
                .build());
    }
    @PostMapping("/base-log/{classId}")
    public ResponseEntity<Void> createBaseLog(@PathVariable String classId, @RequestParam String today) {
        scheduleService.createBaseLog(classId, today);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ResponseModel<?>> getChildrenAttendanceLogBaseOnVehicle(@PathVariable String managerId, @RequestParam String today) {
        return ResponseEntity.ok(ResponseModel.builder()
                .data(attendanceService.getChildrenAttendanceLogBaseOnVehicle(managerId, today))
                .build());
    }

    @PutMapping()
    public ResponseEntity<?> updateAttendance(@RequestBody Log logs) {
        try {
            attendanceService.updateAttendanceStatus(logs);
            return ResponseEntity.ok("Attendance status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update attendance status");
        }
    }
    @PatchMapping("/updateTime/{id}")
    public ResponseEntity<String> updateAttendanceTime(
            @PathVariable String id,
            @RequestParam String type) {

        try {
            attendanceService.updateAttendanceTime(id, type);
            return ResponseEntity.ok("Attendance time updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
