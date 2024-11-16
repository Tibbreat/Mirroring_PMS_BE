package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.entities.Route;
import sep490.g13.pms_be.entities.StopLocation;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.route.AddRouteRequest;
import sep490.g13.pms_be.model.response.route.ListRouteAvailableResponse;
import sep490.g13.pms_be.model.response.route.ListRouteResponse;
import sep490.g13.pms_be.repository.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private StopLocationRepo stopLocationRepo;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private ChildrenRouteRepo childrenRouteRepo;
    @Autowired
    private ChildrenRepo childrenRepo;
    @Autowired
    private ChildrenClassRepo childrenClassRepo;
    @Autowired
    private ClassRepo classRepo;


    public Route createRoute(AddRouteRequest addRouteRequest) {
        Route route = Route.builder()
                .routeName(addRouteRequest.getRouteName())
                .startLocation(addRouteRequest.getStartLocation())
                .endLocation(addRouteRequest.getEndLocation())
                .pickupTime(addRouteRequest.getPickupTime())
                .dropOffTime(addRouteRequest.getDropOffTime())
                .isActive(true)
                .build();

        route = routeRepo.save(route);


        List<StopLocation> stopLocations = new ArrayList<>();
        for (int i = 0; i < addRouteRequest.getStopLocations().size(); i++) {
            String locationName = addRouteRequest.getStopLocations().get(i);

            StopLocation stopLocation = StopLocation.builder()
                    .locationName(locationName)
                    .stopOrder(i + 1)
                    .route(route)
                    .build();

            stopLocations.add(stopLocation);
        }
        stopLocationRepo.saveAll(stopLocations);
        route.setStopLocations(new HashSet<>(stopLocations));
        return route;
    }

    public Page<ListRouteResponse> findAllRoute(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return routeRepo.getAllRoute(pageable);
    }

    public ListRouteResponse getRouteById(String routeId){
         routeRepo.findById(routeId).orElseThrow(() -> new DataNotFoundException("Data not exist"));
         return routeRepo.getRoute(routeId);
    }

    public List<StopLocation> getStopLocationByRouteId(String routeId){
        return stopLocationRepo.getStopLocationByRouteId(routeId);
    }

    public List<Route> getAvailableRoute(){
        return routeRepo.getAvailableRoute();
    }

    @Transactional
    public void changeStatus(String routeId) {
        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new DataNotFoundException("Data not exist"));

        if (route.getIsActive()) {
            // Lấy danh sách các `Vehicle` hiện đang liên kết với `Route` này và xóa `Route` khỏi từng `Vehicle`
            List<String> vehicleIds = vehicleRepo.findAllByRoute(routeId);
            vehicleIds.forEach(vehicleId -> vehicleRepo.removeRoute(vehicleId));

            // Lấy danh sách các `Children` liên kết với `Route` và cập nhật trạng thái
            List<String> childrenIds = childrenRouteRepo.findAllChildrenByRouteId(routeId);
            childrenIds.forEach(childrenId -> {
                childrenRepo.updateTransportServiceStatus(childrenId, false);  // Cập nhật trạng thái dịch vụ vận chuyển
                childrenRepo.removeVehicle(childrenId);  // Xóa liên kết `Vehicle` của `Children`
            });

            // Cập nhật số lượng trẻ đăng ký vận chuyển trong từng `Class`
            List<Classes> classesList = childrenClassRepo.findAllClassByChildrenId(childrenIds);
            classesList.forEach(c -> {
                // Tính số trẻ trong lớp `c` đã đăng ký `Route` này
                int countToRemove = childrenRouteRepo.countChildrenOfClassRegisteredThisRoute(routeId, c.getId());
                int updatedCount = c.getCountChildrenRegisteredTransport() - countToRemove;
                classRepo.updateCountStudentRegisteredTransport(c.getId(), updatedCount);  // Cập nhật số lượng
            });

            // Xóa tất cả các `ChildrenRoute` liên kết với `Route` này sau khi hoàn tất cập nhật
            routeRepo.deleteChildrenRouteByRouteId(routeId);
        }

        // Thay đổi trạng thái của `Route`
        routeRepo.changeStatus(routeId, !route.getIsActive());
    }

}
