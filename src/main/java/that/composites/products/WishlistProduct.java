package that.composites.products;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.composites.AbstractPageComposite;
import that.entities.Product;

public class WishlistProduct extends AbstractPageComposite {
    protected SelenideElement root;
    private By brandLocator = By.className("brand");
    private By productNameLocator = By.className("name");
    private By imageLinkLocator = By.cssSelector(".image-holder img");
    private By priceLocator = By.cssSelector(".price-wrapper .normal");
    private By removeFromWishlistBtnLocator = By.className("remove-wishlist");
    private By moveToBagButtonLocator = By.className("move-to-bag");

    public WishlistProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getInformation() {
        return new Product(root, brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }

    public Boolean isRemoveFromWishlistBtnDisplayed(){
        root.find(removeFromWishlistBtnLocator).hover().shouldBe(Condition.visible);
        return true;
    }

    public void moveToBag() {
        root.hover().find(moveToBagButtonLocator).click();
    }

    public void removeFromWishlist(){
        root.find(removeFromWishlistBtnLocator).hover().click();
    }

}
