package that.entities;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;

import static utils.Utils.getIntegerValueOfPrice;

@Getter
@Setter
public class Product {
    private String brand;
    private String productName;
    private String imageLink;
    private Integer price;
    private Boolean sale;

    public Product(SelenideElement root, By brandLocator, By productNameLocator, By imageLinkLocator, By priceLocator) {
        this.brand = root.find(brandLocator).getText();
        this.productName = root.find(productNameLocator).getText();
        this.imageLink = root.find(imageLinkLocator).getAttribute("src");
        String price = root.find(priceLocator).getText();
        this.price = getIntegerValueOfPrice(price);
    }

    public Product(SelenideElement root, By brandLocator, By productNameLocator, By imageLinkLocator, By priceLocator, By saleTagLocator) {
        this(root, brandLocator, productNameLocator, imageLinkLocator, priceLocator);
        this.sale = !root.find(saleTagLocator).getText().isBlank();
    }

    public Product(SelenideElement brand, SelenideElement productName, SelenideElement imageLink, SelenideElement price) {
        this.brand = brand.getText();
        this.productName = productName.getText();
        this.imageLink = imageLink.getAttribute("src");;
        this.price = getIntegerValueOfPrice(price.getText());
    }
}
