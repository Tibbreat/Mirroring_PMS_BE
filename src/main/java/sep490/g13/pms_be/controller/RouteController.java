package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.Route;
import sep490.g13.pms_be.entities.StopLocation;
import sep490.g13.pms_be.model.request.route.AddRouteRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.model.response.route.ListRouteResponse;
import sep490.g13.pms_be.service.entity.RouteService;

import java.util.List;

@RestController
@RequestMapping("/pms/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("new-route")
    public ResponseEntity<Route> addRoute(@RequestBody AddRouteRequest addRouteRequest) {
        return ResponseEntity.ok(routeService.createRoute(addRouteRequest));
    }

    @GetMapping("all-routes")
    public ResponseEntity<PagedResponseModel<ListRouteResponse>> getAllRoutes(@RequestParam int page) {
        int size = 10;
        Page<ListRouteResponse> routes = routeService.findAllRoute(page -1, size);
        List<ListRouteResponse> routeList = routes.getContent();

        String msg = routeList.isEmpty() ? "Không tìm thấy tuyến nào" : "Tìm thấy " + routeList.size() + " tuyến";
        return ResponseEntity.ok(PagedResponseModel.<ListRouteResponse>builder()
                .msg(msg)
                .listData(routeList)
                .total(routes.getTotalPages())
                .page(page)
                .size(size)
                .build());
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<ListRouteResponse> getRouteById(@PathVariable String routeId){
        return ResponseEntity.ok(routeService.getRouteById(routeId));
    }

    @GetMapping("/stop-location/{routeId}")
    public ResponseEntity<List<StopLocation>> getStopLocationByRouteId(@PathVariable String routeId){
        return ResponseEntity.ok(routeService.getStopLocationByRouteId(routeId));
    }
    @GetMapping("/available")
    public ResponseEntity<List<Route>> getAvailableRoute(){
        return ResponseEntity.ok(routeService.getAvailableRoute());
    }

    @PutMapping("/change-status/{routeId}")
    public ResponseEntity<String> changeStatus(@PathVariable String routeId){
        routeService.changeStatus(routeId);
        return ResponseEntity.ok("Thay đổi trạng thái thành công");
    }
}
