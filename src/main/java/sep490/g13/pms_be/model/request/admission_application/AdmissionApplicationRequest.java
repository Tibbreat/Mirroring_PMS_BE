package sep490.g13.pms_be.model.request.admission_application;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdmissionApplicationRequest {
    //Child information
    private String childName;
    private String childBirthDate;
    private String gender;
    private boolean isDisabled;
    private String disabledNote;

    //Birth address
    private String birthAddressWard;
    private String birthAddressDistrict;
    private String birthAddressProvince;
    private String birthAddressDetail;

    //Current address
    private String childAddressWard;
    private String childAddressDistrict;
    private String childAddressProvince;
    private String childAddressDetail;

    //Father and mother information
    private String fatherName;
    private String fatherPhone;
    private String fatherIdCardNumber;
    private String motherName;
    private String motherPhone;
    private String motherIdCardNumber;

    private String academicYear;
}
