package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.model.response.application.RouteApplication;
import sep490.g13.pms_be.model.response.application.RouteReportCard;
import sep490.g13.pms_be.repository.RouteSubmittedApplicationRepo;

import java.util.List;

@Service
public class ApplicationFormService {

    @Autowired
    private RouteSubmittedApplicationRepo routeSubmittedApplicationRepo;

    public List<RouteApplication> getAllApplicationFormByAcademicYear(String academicYear, String routeId) {
        return routeSubmittedApplicationRepo.findAllByAcademicYear(academicYear, routeId);
    }

    public List<RouteReportCard> getAllRouteReport(String academicYear) {
        return routeSubmittedApplicationRepo.findAllRouteReport(academicYear);
    }
}
