package sep490.g13.pms_be.model.response.children;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenDetailResponse {
    private String childName;
    private LocalDate childBirthDate;
    private String childAddress;
    private Boolean isRegisteredForTransport;
    private Boolean isRegisteredForBoarding;
    private String imageUrl;
    private String birthAddress;
    private String nationality;
    private String religion;
    private String gender;

    private String fatherId;
    private String fatherName;
    private String fatherPhone;

    private String motherId;
    private String motherName;
    private String motherPhone;

    private String id;
}
