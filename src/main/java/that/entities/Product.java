package that.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String brand;
    private String productName;
    private String imageLink;
    private Integer price;

    public Product(String brand, String productName, String imageLink, String price){
        this.brand = brand;
        this.productName = productName;
        this.imageLink = imageLink;
        this.price = getIntegerValueOfPrice(price);
    }

    private Integer getIntegerValueOfPrice(String price){
        String priceValue = price.replaceAll("\\D", "");
        return Integer.parseInt(priceValue);
    }
}
