package that.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryAddress {
    public static DeliveryAddress TEST_ADDRESS = new DeliveryAddress("United Arab Emirates", "Abu Dhabi",
            "ADNEC", "Test Street", "10", "10", "10");
    String country;
    String city;
    String area;
    String street;
    String building;
    String flat;
    String floor;
}
