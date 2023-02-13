package that.composites;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import that.entities.Product;

public class BaseProduct extends AbstractPageComposite{
    protected WebElement root;

    protected BaseProduct(WebDriver driver, WebElement root) {
        super(driver);
        this.root = root;
    }

    protected Product getProductInformation(By brandLocator, By productNameLocator, By imageLinkLocator, By priceLocator){
        WebElement brand = wait
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, brandLocator));
        WebElement productName = wait
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, productNameLocator));
        WebElement imageLink = wait
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, imageLinkLocator));
        WebElement price = wait
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, priceLocator));
        return getProductInformation(brand, productName, imageLink, price);
    }
}
