package that.composites;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.entities.Product;

public class WishlistProduct extends AbstractPageComposite {
    protected SelenideElement root;
    private By brandLocator = By.className("brand");
    private By productNameLocator = By.className("name");
    private By imageLinkLocator = By.cssSelector(".image-holder img");
    private By priceLocator = By.cssSelector(".price-wrapper .normal");

    public WishlistProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getProductInformation() {
        return new Product(root, brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }

}
