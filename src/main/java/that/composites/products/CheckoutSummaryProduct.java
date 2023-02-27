package that.composites.products;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.composites.AbstractPageComposite;
import that.entities.Product;

public class CheckoutSummaryProduct extends AbstractPageComposite {
    private SelenideElement root;
    private By imageLinkLocator = By.cssSelector(".product-image img");
    private By brandLocator = By.cssSelector(".product-info > h4");
    private By productNameLocator = By.cssSelector(".product-info > p");
    private By priceLocator = By.cssSelector(".product-info .price-iota");

    public CheckoutSummaryProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getProductInformation() {
        return new Product(root, brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }
}
