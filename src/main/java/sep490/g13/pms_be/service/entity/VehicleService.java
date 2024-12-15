package sep490.g13.pms_be.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.entities.*;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.route.AddVehicleIntoRouteRequest;
import sep490.g13.pms_be.model.request.vehicle.AddVehicleRequest;
import sep490.g13.pms_be.model.response.vehicle.VehicleAvailableResponse;
import sep490.g13.pms_be.repository.*;
import sep490.g13.pms_be.service.utils.CloudinaryService;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private TransportServiceProviderRepo transportRepo;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private VehicleImageRepo vehicleImageRepo;
    @Autowired
    private RouteRepo routeRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ChildrenRepo childrenRepo;

    @Autowired
    private ChildrenService childrenService;

    public Page<Vehicle> getAllVehicle(String providerId, int page, int size) {
        return vehicleRepo.findAllByTransportId(providerId, PageRequest.of(page, size));
    }

    public List<Vehicle> getAllVehicleByRouteId(String routeId) {
        return vehicleRepo.findAllByRouteId(routeId);
    }

    public Vehicle add(AddVehicleRequest request, List<MultipartFile> images) {
        // Kiểm tra xem provider có tồn tại không
        TransportServiceProvider provider = transportRepo.findById(request.getProviderId())
                .orElseThrow(() -> new DataNotFoundException("Provider not found"));
        if (vehicleRepo.existsByLicensePlate(request.getLicensePlate())) {
            throw new IllegalArgumentException("Thông tin xe có biển số + " + request.getLicensePlate() + " đã tồn tại");
        }
        // Kiểm tra số lượng phương tiện của provider
        int count = vehicleRepo.countByTransport_Id(request.getProviderId());
        if (count >= provider.getTotalVehicle()) {
            throw new IllegalArgumentException("Provider has reached maximum number of vehicle");
        }

        // Tạo đối tượng Vehicle mới
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(request, vehicle);
        vehicle.setCreatedBy(request.getCreatedBy());
        vehicle.setTransport(provider);
        vehicle.setIsActive(Boolean.TRUE);

        // Lưu phương tiện vào cơ sở dữ liệu trước
        Vehicle savedVehicle = vehicleRepo.save(vehicle);

        // Xử lý tệp hình ảnh (nếu có)
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        // Upload ảnh lên Cloudinary và nhận URL
                        String imagePath = cloudinaryService.saveImage(image);

                        // Tạo đối tượng VehicleImage và liên kết với Vehicle
                        VehicleImage vehicleImage = new VehicleImage();
                        vehicleImage.setImageUrl(imagePath);
                        vehicleImage.setImageType(image.getContentType());
                        vehicleImage.setVehicle(savedVehicle);

                        // Lưu hình ảnh vào cơ sở dữ liệu
                        vehicleImageRepo.save(vehicleImage);

                    } catch (Exception e) {
                        throw new RuntimeException("Error uploading vehicle image: " + e.getMessage());
                    }
                }
            }
        }

        // Trả về đối tượng Vehicle đã lưu, cùng với các hình ảnh nếu có
        return savedVehicle;
    }

    @Transactional
    public void updateStatus(String vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found"));

        vehicleRepo.updateStatus(vehicleId, !vehicle.getIsActive());
    }

    public List<VehicleAvailableResponse> getVehicleAvailable() {
        return vehicleRepo.getVehicleAvailable();
    }

    @Transactional
    public void registerVehicleToRoute(List<AddVehicleIntoRouteRequest> requests, String routeId) {
        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new DataNotFoundException("Route not found with ID: " + routeId));

        requests.forEach(request -> {
            Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                    .orElseThrow(() -> new DataNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));
            User manager = userRepo.findById(request.getManagerId())
                    .orElseThrow(() -> new DataNotFoundException("Manager not found with ID: " + request.getManagerId()));
            vehicle.setRoute(route);
            vehicle.setManager(manager);
            vehicleRepo.save(vehicle);
        });
    }

    @Transactional
    public void unsubscribeRoute(String vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found"));
        if (vehicle.getRoute() == null) {
            throw new IllegalArgumentException("Vehicle is not subscribed to any route.");
        }

        //Hủy trạng thái đăng ký đưa đón của trẻ.
        List<String> childrenIds = childrenRepo.getChildrenIdsByVehicleId(vehicleId);
        for (String childrenId : childrenIds) {
            childrenService.updateServiceStatus(childrenId, "transport", null, null);
        }
        vehicle.setRoute(null);
        vehicle.setManager(null);
        vehicleRepo.save(vehicle);
    }

    public Vehicle findVehicleByManagerId(String managerId) {
        return vehicleRepo.findVehicleByManagerId(managerId);
    }
}
