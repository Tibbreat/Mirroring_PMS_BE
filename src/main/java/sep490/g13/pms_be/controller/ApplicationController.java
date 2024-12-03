package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.model.request.admission_application.ApprovedTransportApplication;
import sep490.g13.pms_be.model.response.application.RouteApplication;
import sep490.g13.pms_be.model.response.application.RouteReportCard;
import sep490.g13.pms_be.service.entity.ApplicationFormService;
import sep490.g13.pms_be.service.entity.ChildrenService;

import java.util.List;

@RestController
@RequestMapping("/pms/application")
public class ApplicationController {

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private ChildrenService childrenService;
    @GetMapping("/get-all-application-form/{academicYear}")
    public List<RouteApplication> getAllApplicationFormByAcademicYear(@PathVariable String academicYear,
                                                                      @RequestParam(required = false) String routeId) {
        return applicationFormService.getAllApplicationFormByAcademicYear(academicYear, routeId);
    }
    @GetMapping("/get-all-route-report/{academicYear}")
    public List<RouteReportCard> getAllRouteReport(@PathVariable String academicYear) {
        return applicationFormService.getAllRouteReport(academicYear);
    }

    @PutMapping("/approve-application")
    public void approveApplication(@RequestBody ApprovedTransportApplication request) {
        childrenService.registerTransport(request);
    }
}
