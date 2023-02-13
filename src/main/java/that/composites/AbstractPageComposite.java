package that.composites;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import that.entities.Product;

import java.time.Duration;
import java.util.List;

public class AbstractPageComposite {
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected AbstractPageComposite(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        PageFactory.initElements(driver, this);
    }

    protected Boolean areListItemsClickable(List<WebElement> listOfItems) {
        return listOfItems.stream().allMatch(WebElement::isEnabled);
    }

    protected Boolean isItemClickable(WebElement item) {
        return item.isEnabled();
    }

    protected WebElement getWebElementByName(List<WebElement> listOfItems, String itemName) {
        return listOfItems.stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .get();
    }

    protected void clickListItem(List<WebElement> listOfItems, String itemName) {
        getWebElementByName(listOfItems, itemName).click();
    }

    protected void clickItem(WebElement item){
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();
    }

    protected String getItemText(WebElement item){
        return wait.until(ExpectedConditions.visibilityOf(item)).getText();
    }

    protected void moveToListItem(List<WebElement> listOfItems, String itemName) {
        Actions action = new Actions(driver);
        WebElement item = getWebElementByName(listOfItems, itemName);
        action.moveToElement(item).perform();
    }

    protected void moveToItem(WebElement item){
        Actions action = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOf(item));
        action.moveToElement(item).perform();
    }

    protected Boolean waitForTitle(String title) {
        return wait.until(ExpectedConditions.titleIs(title));
    }

    protected Boolean waitForTitleContains(String titlePart) {
        return wait.until(ExpectedConditions.titleContains(titlePart));
    }

    public Product getProductInformation(WebElement brand, WebElement productName, WebElement imageLink, WebElement price){
        return new Product(getItemText(brand), getItemText(productName), imageLink.getAttribute("src"), getItemText(price));
    }
}
