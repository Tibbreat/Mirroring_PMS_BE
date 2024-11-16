package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.FoodServiceProvider;
import sep490.g13.pms_be.model.request.foodsupplier.AddProviderRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.service.entity.FoodServiceProviderService;

import java.util.List;

@RestController
@RequestMapping("/pms/food-service-provider")
public class FoodServiceProviderController {

    @Autowired
    private FoodServiceProviderService foodServiceProviderService;

    @GetMapping
    public ResponseEntity<PagedResponseModel<FoodServiceProvider>> getAllProvider(@RequestParam int page) {
        Page<FoodServiceProvider> providers = foodServiceProviderService.getAllProvider(page - 1, 10);
        List<FoodServiceProvider> results = providers.getContent();
        String msg = results.isEmpty() ? "No provider found" : "Get all provider successfully";
        return ResponseEntity.ok(PagedResponseModel.<FoodServiceProvider>builder()
                .page(page)
                .size(10)
                .msg(msg)
                .total(providers.getTotalElements())
                .listData(results)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<FoodServiceProvider> add(@RequestBody AddProviderRequest request) {
        return ResponseEntity.ok(foodServiceProviderService.add(request));
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<FoodServiceProvider> getDetail(@PathVariable String providerId) {
        return ResponseEntity.ok(foodServiceProviderService.getDetail(providerId));
    }

    @PutMapping("/status/{providerId}")
    public ResponseEntity<Void> updateStatus(@PathVariable String providerId) {
        foodServiceProviderService.updateStatus(providerId);
        return ResponseEntity.ok().build();
    }
}

