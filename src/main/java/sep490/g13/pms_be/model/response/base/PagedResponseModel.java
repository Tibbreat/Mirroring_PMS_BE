package sep490.g13.pms_be.model.response.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseModel<T> {
    private int page;
    private int size;
    private long total;
    private String msg;
    private List<T> listData;
}
