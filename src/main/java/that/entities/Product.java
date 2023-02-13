package that.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private String brand;
    private String productName;
    private String imageLink;
    private String price;
}
