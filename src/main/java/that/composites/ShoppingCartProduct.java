package that.composites;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import that.entities.Product;

public class ShoppingCartProduct extends BaseProduct{
    private By imageLinkLocator = By.cssSelector(".cx-image-container img");
    private By brandLocator = By.className("cx-name");
    private By productNameLocator = By.className("cx-code");
    private By priceLocator = By.cssSelector(".cx-price .cx-value:first-of-type");
    private By quantitySelectLocator = By.className("quantity-select");
    public ShoppingCartProduct(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public Product getProductInformation(){
        return getProductInformation(brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }

    public int getProductCount(){
        Select productQuantitySelect =
                new Select(wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, quantitySelectLocator)));
        String productCount = productQuantitySelect.getFirstSelectedOption().getText();
        return Integer.parseInt(productCount);
    }

    public void selectMaxAvailableProductCount(){
        Select productQuantitySelect =
                new Select(wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, quantitySelectLocator)));
        Integer maxProductCount = productQuantitySelect
                .getOptions()
                .stream()
                .map(option -> Integer.parseInt(option.getText()))
                .max(Integer::compare)
                .get();
        productQuantitySelect.selectByValue(maxProductCount.toString());
    }
}
