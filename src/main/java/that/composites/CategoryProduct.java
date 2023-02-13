package that.composites;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import that.entities.Product;

public class CategoryProduct extends BaseProduct{

    private By brandLocator = By.className("item-title");
    private By productNameLocator = By.className("description");
    private By mainImageLinkLocator = By.xpath("(//*[@class='custom-media-banner']//img)[1]");
    private By priceLocator = By.className("price-box");
    private By wishlistButtonLocator = By.className("heart");

    public CategoryProduct(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public Product getProductInformation(){
        return getProductInformation(brandLocator, productNameLocator, mainImageLinkLocator, priceLocator);
    }

    public void clickProduct() {
        clickItem(root);
    }

    public void scrollToProduct(){
        WebElement itemTitle = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, brandLocator));
        moveToItem(itemTitle);
    }

    public void addToWishlist(){
        WebElement wishlistButton = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, wishlistButtonLocator));
        wishlistButton.click();
    }

}
