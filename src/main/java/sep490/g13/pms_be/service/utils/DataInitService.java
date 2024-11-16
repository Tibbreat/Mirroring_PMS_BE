package sep490.g13.pms_be.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.entities.*;
import sep490.g13.pms_be.repository.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DataInitService {

    @Autowired
    private RouteSubmittedApplicationRepo routeSubmittedApplicationRepo;

    @Autowired
    private ChildrenRepo childrenRepo;

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private RouteRepo routeRepo;
    @Autowired
    private StopLocationRepo stopLocationRepo;


    private List<Children> selectStudents(List<Children> students, double minPercentage, double maxPercentage) {
        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException("Student list cannot be null or empty.");
        }

        int totalStudents = students.size();
        int minCount = (int) Math.ceil(totalStudents * (minPercentage / 100));
        int maxCount = (int) Math.floor(totalStudents * (maxPercentage / 100));

        Collections.shuffle(students);

        int selectedCount = minCount + (int) (Math.random() * ((maxCount - minCount) + 1));
        return students.stream()
                .limit(selectedCount)
                .collect(Collectors.toList());
    }

    public void initRouteSubmittedApplication() {
        List<Classes> classes = classRepo.findClassesByAcademicYear("2024-2025");
        List<Route> routes = new ArrayList<>();
        List<Route> allRoutes = routeRepo.findAll();

        // Add specific routes for assignment
        routes.add(allRoutes.get(1));
        routes.add(allRoutes.get(2));

        Random random = new Random();

        for (Classes c : classes) {
            // Fetch all children in the class
            List<Children> students = childrenRepo.findChildrenByClassId(c.getId());

            // Randomly select 40-70% of students
            List<Children> selectedStudents = selectStudents(students, 40, 70);

            for (Children s : selectedStudents) {
                // Randomly assign a route to the student
                Route assignedRoute = routes.get(random.nextInt(routes.size()));

                // Fetch stop locations for the selected route
                List<StopLocation> stopLocations = stopLocationRepo.getStopLocationByRouteId(assignedRoute.getId());

                // Randomly assign a stop location
                StopLocation assignedStopLocation = stopLocations.get(random.nextInt(stopLocations.size()));

                // Save the RouteSubmittedApplication
                routeSubmittedApplicationRepo.save(
                        RouteSubmittedApplication.builder()
                                .children(s)
                                .childrenName(s.getChildName())
                                .route(assignedRoute)
                                .routeName(assignedRoute.getRouteName())
                                .stopLocation(assignedStopLocation)
                                .stopLocationName(assignedStopLocation.getLocationName())
                                .academicYear("2024-2025")
                                .status("PENDING")
                                .build()
                );
            }
        }
    }
}
