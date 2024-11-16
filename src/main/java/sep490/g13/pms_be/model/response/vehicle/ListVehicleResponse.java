package sep490.g13.pms_be.model.response.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListVehicleResponse {
    private String vehicleName;
    private String manufacturer;
    private int numberOfSeats;
    private String color;
    private String licensePlate;
    private String driverName;
    private String driverPhone;
    private Boolean isActive;
}
