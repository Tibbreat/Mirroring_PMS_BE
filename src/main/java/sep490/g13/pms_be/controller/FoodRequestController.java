package sep490.g13.pms_be.controller;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import jakarta.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.FoodRequest;
import sep490.g13.pms_be.entities.FoodServiceProvider;
import sep490.g13.pms_be.entities.School;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.model.request.PDFData.FoodRequestDataModel;
import sep490.g13.pms_be.model.request.food.AddFoodRequest;
import sep490.g13.pms_be.model.response.base.PagedResponseModel;
import sep490.g13.pms_be.model.response.base.ResponseModel;
import sep490.g13.pms_be.model.response.food.ListFoodResponse;
import sep490.g13.pms_be.model.response.food.ListRequestItemsResponse;
import sep490.g13.pms_be.repository.FoodRequestRepo;
import sep490.g13.pms_be.repository.FoodServiceProviderRepo;
import sep490.g13.pms_be.repository.SchoolRepo;
import sep490.g13.pms_be.service.entity.FoodRequestService;
import sep490.g13.pms_be.utils.ValidationUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/pms/food-request")
public class FoodRequestController {

    @Autowired
    private FoodRequestService foodRequestService;

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private FoodServiceProviderRepo foodServiceProviderRepo;

    @Autowired
    private FoodRequestRepo foodRequestRepo;


    @PostMapping("/add")
    public ResponseEntity<ResponseModel<?>> add(@RequestBody @Valid AddFoodRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(
                    ResponseModel.builder()
                            .message("Thông tin lớp học không hợp lệ")
                            .data(validationErrors)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .message("Thêm lớp học thành công")
                        .data(foodRequestService.add(request))
                        .build()
        );
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<PagedResponseModel<ListFoodResponse>> getAllByProviderId(@PathVariable String providerId, @RequestParam int page) {
        Page<ListFoodResponse> foodRequests = foodRequestService.getAllByProviderId(providerId, page - 1, 10);
        List<ListFoodResponse> results = foodRequests.getContent();
        String msg = results.isEmpty() ? "No data found" : "Get all data successfully";
        return ResponseEntity.ok(PagedResponseModel.<ListFoodResponse>builder()
                .page(page)
                .size(10)
                .msg(msg)
                .total(foodRequests.getTotalElements())
                .listData(results)
                .build());
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ListRequestItemsResponse>> getById(@PathVariable String requestId) {
        return ResponseEntity.ok(foodRequestService.getItems(requestId));
    }


    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> changeStatus(@PathVariable String requestId,
                                          @RequestParam String status,
                                          @RequestBody(required = false) FoodRequestDataModel data) {
        if (status.equals("APPROVED")) {
            School school = schoolRepo.findFirstByOrderByIdAsc();
            FoodServiceProvider provider = foodServiceProviderRepo.findById(data.getProviderId()).orElseThrow(() -> new DataNotFoundException("Provider not found"));
            FoodRequest request = foodRequestRepo.findById(requestId).orElseThrow(() -> new DataNotFoundException("Request not found"));
            String contractNumber = foodRequestService.changeStatusOfRequest(requestId, status);
            try {
                InputStream pdfTemplate = getClass().getResourceAsStream("/tmp/food-request-template.pdf");
                if (pdfTemplate == null) {
                    throw new RuntimeException("File PDF template không tồn tại!");
                }

                PdfReader pdfReader = new PdfReader(pdfTemplate);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfStamper stamper = new PdfStamper(pdfReader, outputStream);
                AcroFields form = stamper.getAcroFields();
                BaseFont baseFont = BaseFont.createFont("font/arial-unicode-ms-regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                form.addSubstitutionFont(baseFont);

                form.setField("contractCity", school.getSchoolAddressCity());
                form.setField("contractNumber", contractNumber);
                form.setField("schoolAddress", school.getSchoolAddress());
                form.setField("providerName", provider.getProviderName());
                form.setField("providerAddress", provider.getProviderAddress());
                form.setField("providerTaxCode", provider.getProviderTaxCode());
                form.setField("providerPhone", provider.getProviderPhone());
                form.setField("representativeName", provider.getRepresentativeName());
                form.setField("representativePosition", provider.getRepresentativePosition());
                form.setField("schoolName", school.getSchoolName());
                form.setField("schoolPhone", school.getPhoneContact());
                form.setField("principal", school.getPrincipal().getFullName());
                form.setField("neededDate", request.getDayNeeded());
                form.setField("bankName", provider.getBankName());
                form.setField("bankAccountNumber", provider.getBankAccountNumber());
                form.setField("beneficiaryName", provider.getBeneficiaryName());

                stamper.setFormFlattening(true);
                stamper.close();

                String pdfBase64 = Base64.encodeBase64String(outputStream.toByteArray());

                return ResponseEntity.ok().body(Map.of("pdfBase64", pdfBase64));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
        } else {
            foodRequestService.changeStatusOfRequest(requestId, status);
        }
        return ResponseEntity.ok().build();
    }

}
