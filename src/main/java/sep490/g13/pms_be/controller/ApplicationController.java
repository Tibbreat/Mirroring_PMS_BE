package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.model.response.application.RouteApplication;
import sep490.g13.pms_be.service.entity.ApplicationFormService;

import java.util.List;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    @Autowired
    private ApplicationFormService applicationFormService;

    @GetMapping("/get-all-application-form/{academicYear}")
    public List<RouteApplication> getAllApplicationFormByAcademicYear(@PathVariable String academicYear,
                                                                      @RequestParam(required = false) String routeId) {
        return applicationFormService.getAllApplicationFormByAcademicYear(academicYear, routeId);
    }
}
