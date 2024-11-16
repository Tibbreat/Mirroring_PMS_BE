package sep490.g13.pms_be.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sep490.g13.pms_be.entities.FoodRequest;
import sep490.g13.pms_be.entities.FoodRequestItem;
import sep490.g13.pms_be.entities.FoodServiceProvider;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.food.AddFoodRequest;
import sep490.g13.pms_be.model.response.food.ListFoodResponse;
import sep490.g13.pms_be.model.response.food.ListRequestItemsResponse;
import sep490.g13.pms_be.repository.FoodRequestItemRepo;
import sep490.g13.pms_be.repository.FoodRequestRepo;
import sep490.g13.pms_be.repository.FoodServiceProviderRepo;
import sep490.g13.pms_be.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodRequestService {

    @Autowired
    private FoodRequestRepo foodRequestRepo;

    @Autowired
    private FoodRequestItemRepo foodRequestItemRepo;

    @Autowired
    private FoodServiceProviderRepo foodServiceProviderRepo;

    public FoodRequest add(AddFoodRequest request) {
        // Lấy thông tin provider từ database
        FoodServiceProvider provider = foodServiceProviderRepo.findById(request.getProviderId())
                .orElseThrow(() -> new DataNotFoundException("Provider not found"));

        // Tạo mới FoodRequest
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setDayNeeded(request.getDayNeeded());
        foodRequest.setFoodServiceProvider(provider);
        foodRequest.setCreatedBy(request.getCreatedBy());
        foodRequest.setStatus("PENDING");


        FoodRequest savedRequest = foodRequestRepo.save(foodRequest);

        List<FoodRequestItem> items = request.getFoodRequestItems().stream().map(itemRequest -> {
            FoodRequestItem item = new FoodRequestItem();
            item.setFoodName(itemRequest.getFoodName());
            item.setQuantity(itemRequest.getQuantity());
            item.setNote(itemRequest.getNote());
            item.setFoodRequest(savedRequest);
            return item;
        }).collect(Collectors.toList());

        // Lưu các FoodRequestItem vào cơ sở dữ liệu
        foodRequestItemRepo.saveAll(items);

        return savedRequest;
    }


    public Page<ListFoodResponse> getAllByProviderId(String providerId, int page, int size) {
        return foodRequestRepo.getAllByProviderId(providerId, PageRequest.of(page, size));
    }

    public List<ListRequestItemsResponse> getItems(String requestId) {
        foodRequestRepo.findById(requestId).orElseThrow(() -> new DataNotFoundException("Request not found"));
        return foodRequestItemRepo.findByFoodRequestId(requestId);
    }

    @Transactional
    public String changeStatusOfRequest(String foodRequestId, String status) {
        FoodRequest request = foodRequestRepo.findById(foodRequestId).orElseThrow(() -> new DataNotFoundException("Request not found"));
        if (request.getStatus().equals("APPROVED") || request.getStatus().equals("CANCELED")) {
            throw new DataNotFoundException("Request has been approved or canceled");
        }
        String contractNumber = StringUtils.randomNumber(3);
        foodRequestRepo.changeStatusOfRequest(foodRequestId, status, contractNumber );
        return contractNumber;
    }
}
