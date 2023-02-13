package that.composites;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import that.entities.Product;

public class WishlistProduct extends BaseProduct{
    private By brandLocator = By.className("brand");
    private By productNameLocator = By.className("name");
    private By imageLinkLocator = By.cssSelector(".image-holder img");
    private By priceLocator = By.cssSelector(".price-wrapper .normal");

    public WishlistProduct(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public Product getProductInformation(){
        return getProductInformation(brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }

}
