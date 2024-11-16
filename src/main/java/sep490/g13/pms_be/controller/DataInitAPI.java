package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sep490.g13.pms_be.service.utils.DataInitService;

@RestController
@RequestMapping("/api/data-init")
public class DataInitAPI {

    @Autowired
    private DataInitService dataInitService;

    @RequestMapping("/init-route-submitted-application")
    public void initRouteSubmittedApplication() {
        dataInitService.initRouteSubmittedApplication();
    }
}
