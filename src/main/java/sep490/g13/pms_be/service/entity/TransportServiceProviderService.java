package sep490.g13.pms_be.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.Classes;
import sep490.g13.pms_be.entities.TransportServiceProvider;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.transportsupplier.AddTransportProviderRequest;
import sep490.g13.pms_be.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TransportServiceProviderService {

    @Autowired
    private TransportServiceProviderRepo transportServiceProviderRepo;

    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private ChildrenRepo childrenRepo;
    @Autowired
    private RouteRepo routeRepo;
    @Autowired
    private ChildrenClassRepo childrenClassRepo;
    @Autowired
    private ClassRepo classRepo;

    public TransportServiceProvider add(AddTransportProviderRequest request) {
        TransportServiceProvider provider = new TransportServiceProvider();
        BeanUtils.copyProperties(request, provider);
        provider.setCreatedBy(request.getCreatedBy());
        provider.setIsActive(true);
        return transportServiceProviderRepo.save(provider);
    }

    public Page<TransportServiceProvider> getAllProvider(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transportServiceProviderRepo.getAllProvider(pageable);
    }

    public TransportServiceProvider getDetail(String providerId) {
        return transportServiceProviderRepo.findById(providerId).orElseThrow(() -> new DataNotFoundException("Provider not found"));
    }

    @Transactional
    public void updateStatus(String providerId) {
        TransportServiceProvider provider = transportServiceProviderRepo.findById(providerId)
                .orElseThrow(() -> new DataNotFoundException("Provider not found"));

        boolean isProviderActive = provider.getIsActive();
        List<String> vehicleIds = vehicleRepo.findAllByTransportId(providerId)
                .stream()
                .distinct()
                .collect(Collectors.toList());

        if (isProviderActive) {
            // Lấy danh sách tất cả các ID trẻ liên quan đến các phương tiện của nhà cung cấp
            List<String> childrenIds = childrenRepo.findAllChildrenByVehicleId(vehicleIds)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            // Lấy danh sách các lớp liên quan đến trẻ
            List<Classes> classes = childrenClassRepo.findAllClassByChildrenId(childrenIds)
                    .stream()
                    .distinct()
                    .toList();

            Set<String> processedClasses = new HashSet<>();

            vehicleIds.forEach(vehicleId -> {
                classes.forEach(c -> {
                    if (!processedClasses.contains(c.getId())) {
                        // Đếm số lượng trẻ đăng ký với phương tiện trong lớp
                        int currentChildrenRegisteredVehicleOfTransport = childrenClassRepo.countStudentRegisteredTransport(c.getId(), providerId);

                        // Cập nhật số lượng trẻ đăng ký
                        classRepo.updateCountStudentRegisteredTransport(c.getId(),
                                c.getCountChildrenRegisteredTransport() - currentChildrenRegisteredVehicleOfTransport);

                        processedClasses.add(c.getId());
                    }
                });

                // Xóa đăng ký của trẻ với phương tiện và hủy lộ trình
                childrenIds.forEach(childrenId -> routeRepo.deleteChildrenRouteByChildrenId(childrenId));
                vehicleRepo.removeRoute(vehicleId); // Gỡ lộ trình khỏi phương tiện
                vehicleRepo.updateStatus(vehicleId, false); // Vô hiệu hóa phương tiện
                childrenRepo.cancelVehicleRegistration(vehicleId); // Hủy đăng ký phương tiện của trẻ
            });

        } else {
            // Kích hoạt lại tất cả các phương tiện cho nhà cung cấp
            vehicleIds.forEach(vehicleId -> vehicleRepo.updateStatus(vehicleId, true));
        }

        // Chuyển đổi trạng thái hoạt động của nhà cung cấp
        transportServiceProviderRepo.updateProviderStatus(providerId, !isProviderActive);
    }



}
