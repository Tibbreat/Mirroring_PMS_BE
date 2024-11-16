package sep490.g13.pms_be.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.FoodServiceProvider;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.foodsupplier.AddProviderRequest;

import sep490.g13.pms_be.repository.FoodServiceProviderRepo;


@Service
public class FoodServiceProviderService {

    @Autowired
    private FoodServiceProviderRepo foodServiceProviderRepo;

    public FoodServiceProvider add(AddProviderRequest request) {
        FoodServiceProvider provider = new FoodServiceProvider();
        BeanUtils.copyProperties(request, provider);
        provider.setIsActive(true);
        provider.setCreatedBy(request.getCreatedBy());
        return foodServiceProviderRepo.save(provider);
    }

    public Page<FoodServiceProvider> getAllProvider(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foodServiceProviderRepo.getAllProvider(pageable);
    }

    public FoodServiceProvider getDetail(String providerId) {
        return foodServiceProviderRepo.findById(providerId).orElseThrow(() -> new DataNotFoundException("Provider not found"));
    }

    @Transactional
    public void updateStatus(String providerId) {
        FoodServiceProvider provider = foodServiceProviderRepo.findById(providerId).orElseThrow(() -> new DataNotFoundException("Provider not found"));
        foodServiceProviderRepo.updateProviderStatus(providerId, !provider.getIsActive());
    }
}