package sep490.g13.pms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.TransportServiceProvider;
import sep490.g13.pms_be.model.request.transportsupplier.AddTransportProviderRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;

import sep490.g13.pms_be.service.entity.TransportServiceProviderService;

import java.util.List;

@RestController
@RequestMapping("/pms/transport-service-provider")
public class TransportServiceProviderController {

    @Autowired
    private TransportServiceProviderService transportServiceProviderService;

    @PostMapping("/add")
    public ResponseEntity<TransportServiceProvider> add(@RequestBody AddTransportProviderRequest request) {
        return ResponseEntity.ok(transportServiceProviderService.add(request));
    }

    @GetMapping
    public ResponseEntity<PagedResponseModel<TransportServiceProvider>> getAllProvider(@RequestParam int page) {
        Page<TransportServiceProvider> providers = transportServiceProviderService.getAllProvider(page - 1, 10);
        List<TransportServiceProvider> results = providers.getContent();
        String msg = results.isEmpty() ? "No provider found" : "Get all provider successfully";
        return ResponseEntity.ok(PagedResponseModel.<TransportServiceProvider>builder()
                .page(page)
                .size(10)
                .msg(msg)
                .total(providers.getTotalElements())
                .listData(results)
                .build());
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<TransportServiceProvider> getDetail(@PathVariable String providerId) {
        return ResponseEntity.ok(transportServiceProviderService.getDetail(providerId));
    }


    @PutMapping("/status/{providerId}")
    public ResponseEntity<Void> updateStatus(@PathVariable String providerId) {
        transportServiceProviderService.updateStatus(providerId);
        return ResponseEntity.ok().build();
    }

}