package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle extends Auditable<String> {
    private String vehicleName;
    private String manufacturer;
    private int numberOfSeats;
    private String color;
    private String licensePlate;
    private String driverName;
    private String driverPhone;
    private int numberChildrenRegistered;
    private Boolean isActive;

    @ManyToOne
    @JsonBackReference
    private TransportServiceProvider transport;


    @OneToMany(mappedBy = "vehicle")
    @JsonManagedReference
    private List<VehicleImage> images;

    @ManyToOne
    @JsonBackReference
    private Route route;

    @OneToOne
    @JsonManagedReference
    private User manager;
}