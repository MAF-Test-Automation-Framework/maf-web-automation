package that.composites.products;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.composites.AbstractPageComposite;
import that.entities.Product;

public class CategoryProduct extends AbstractPageComposite {
    private SelenideElement root;
    private By brandLocator = By.className("item-title");
    private By productNameLocator = By.className("description");
    private By mainImageLinkLocator = By.xpath("(//*[@class='custom-media-banner']//img)[1]");
    private By priceLocator = By.className("price-box");
    private By wishlistButtonLocator = By.className("heart");
    private By saleTagLocator = By.className("tag-wrapper");

    public CategoryProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getProductInformation() {
        return new Product(root, brandLocator, productNameLocator, mainImageLinkLocator, priceLocator, saleTagLocator);
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
