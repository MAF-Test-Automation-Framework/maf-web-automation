package that.composites;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.entities.Product;

public class CategoryProduct extends AbstractPageComposite {
    private SelenideElement root;
    private By brandLocator = By.className("item-title");
    private By productNameLocator = By.className("description");
    private By mainImageLinkLocator = By.xpath("(//*[@class='custom-media-banner']//img)[1]");
    private By priceLocator = By.className("price-box");
    private By wishlistButtonLocator = By.className("heart");

    public CategoryProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getProductInformation() {
        return getProductInformation(root, brandLocator, productNameLocator, mainImageLinkLocator, priceLocator);
    }

    public void clickProduct() {
        root.click();
    }

    public void scrollToProduct() {
        root.scrollIntoView(false);
    }

    public void addToWishlist() {
        root.find(wishlistButtonLocator).hover().click();
    }

}
