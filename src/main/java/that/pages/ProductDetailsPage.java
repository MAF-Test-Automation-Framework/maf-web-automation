package that.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import that.entities.Product;

import java.util.List;

public class ProductDetailsPage extends AbstractPage {
    public static final String PRODUCT_ID_PREFIX = "Product ID:";

    private By productImagesLocator = By.cssSelector(".slider-item img");

    @FindBy(className = "breadcrumb-wrapper")
    private WebElement breadCrumb;

    @FindBy(className = "tab-control")
    private List<WebElement> detailsSectionTabs;

    @FindBy(className = "tab-content")
    private List<WebElement> detailsSectionTabsContent;

    @FindBy(css = ".productParams p:first-child")
    private WebElement productId;

    @FindBy(className = "product-info-link")
    private WebElement productDetailsLink;

    @FindBy(css = ".AddToCart [type='submit']")
    private WebElement addToCartButton;

    @FindBy(tagName = "cx-add-to-wishlist")
    private WebElement addToWishlistButton;

    @FindBy(className = "product-name")
    private WebElement productName;

    @FindBy(className = "brand-name")
    private WebElement brand;

    @FindBy(css = ".price:first-of-type")
    private WebElement price;

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getPDPBreadCrumb() {
        return getItemText(breadCrumb);
    }

    public Boolean isPDTabContentVisible(int tabIndex) {
        return detailsSectionTabsContent.get(tabIndex).isDisplayed();
    }

    public String getProductId() {
        return getItemText(productId).replace(PRODUCT_ID_PREFIX, "").trim();
    }

    public void clickProductDetailsTabByIndex(int tabIndex) {
        wait
                .until(ExpectedConditions.visibilityOfAllElements(detailsSectionTabs))
                .get(tabIndex)
                .click();
    }

    public void clickProductDetailsLink() {
        clickItem(productDetailsLink);
    }

    public void clickAddToCartButton() {
        clickItem(addToCartButton);
    }

    public void clickAddToWishlistButton() {
        clickItem(addToWishlistButton);
    }

    public Boolean isAddToCartButtonDisabled() {
        return wait.until(d -> !addToCartButton.isEnabled());
    }

    public Product getProductInformation() {
        WebElement imageLink = wait
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(productImagesLocator, 1))
                .get(0);
        return getProductInformation(brand, productName, imageLink, price);
    }

}
