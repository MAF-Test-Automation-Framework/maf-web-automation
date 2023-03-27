package that.composites.products;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.composites.AbstractPageComposite;
import that.entities.Product;
import utils.Utils;

public class CategoryProduct extends AbstractPageComposite {
    private SelenideElement root;
    private By brandLocator = By.className("item-title");
    private By productNameLocator = By.className("description");
    private By mainImageLinkLocator = By.xpath("(//*[@class='custom-media-banner']//img)[1]");
    private By priceLocator = By.className("price-box");
    private By wishlistButtonLocator = By.className("heart");
    private By saleTagLocator = By.className("tag-wrapper");
    private By colorsLocator = By.cssSelector(".images-container img");

    public CategoryProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getInformation() {
        return new Product(root, brandLocator, productNameLocator, mainImageLinkLocator, priceLocator, saleTagLocator);
    }

    public void goToProductDetailsPage() {
        root.click();
    }

    public void scrollTo() {
        root.scrollIntoView(false);
    }

    public void addToWishlist() {
        root.find(wishlistButtonLocator).hover().click();
    }

    public int getColorsNumber(){
        return root.findAll(colorsLocator).size();
    }

    public int getPrice(){
        return Utils.getIntegerValueOfPrice(root.find(priceLocator).getText());
    }
}
