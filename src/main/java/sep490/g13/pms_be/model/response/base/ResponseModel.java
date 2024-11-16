package sep490.g13.pms_be.model.response.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sep490.g13.pms_be.entities.Children;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    private String message;
    private T data;

    public ResponseModel(T data) {
    }
}
