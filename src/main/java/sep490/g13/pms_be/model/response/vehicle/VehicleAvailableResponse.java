package sep490.g13.pms_be.model.response.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAvailableResponse {
    private String vehicleId;
    private String vehicleName;
    private String manufacturer;
    private int numberOfSeats;
    private String color;
    private String licensePlate;
    private String providerName;

}
