package sep490.g13.pms_be.model.request.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableVehicleOptions {
    private String id;
    private String vehicleName;
    private int numberOfSeats;
    private int availableSeats;
    private double distance;
}
