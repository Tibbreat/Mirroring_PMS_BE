package sep490.g13.pms_be.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.model.request.user.AddUserRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationshipRequest {
        private AddUserRequest parent;
        private String relationship;
        private Boolean isRepresentative;
}
