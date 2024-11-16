package sep490.g13.pms_be.model.response.children;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferClassRequest {
    private String childrenId;
    private String oldClassId;
    private String newClassId;
}
