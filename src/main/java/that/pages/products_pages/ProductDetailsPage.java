package that.pages.products_pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.entities.Product;
import that.pages.AbstractPage;

public class ProductDetailsPage extends AbstractPage {
    public static final String PRODUCT_ID_PREFIX = "Product ID:";
    private By colorsDropdownOptions = By.cssSelector("app-product-variant-color-selector .ng-option");
    @FindBy(css = ".slider-item img")
    private ElementsCollection productImages;

    @FindBy(className = "product-name")
    private SelenideElement productName;

    @FindBy(className = "brand-name")
    private SelenideElement brand;

    @FindBy(css = ".price:first-of-type")
    private SelenideElement price;

    @FindBy(className = "product-info-link")
    private SelenideElement productDetailsLink;

    @FindBy(className = "tab-control")
    private ElementsCollection detailsSectionTabs;

    @FindBy(className = "tab-content")
    private ElementsCollection detailsSectionTabsContent;

    @FindBy(css = ".productParams p:first-child")
    private SelenideElement productId;

    @FindBy(css = ".AddToCart [type='submit']")
    private SelenideElement addToCartButton;

    @FindBy(tagName = "cx-add-to-wishlist")
    private SelenideElement addToWishlistButton;

    @FindBy(className = "stock-indicator")
    private SelenideElement stockIndicator;

    @FindBy(tagName = "app-product-variant-color-selector")
    private SelenideElement colorsDropdown;

    public Boolean isPDTabContentVisible(int tabIndex) {
        return detailsSectionTabsContent.get(tabIndex).isDisplayed();
    }

    public String getProductId() {
        return productId.getText().replace(PRODUCT_ID_PREFIX, "").trim();
    }

    public void clickProductDetailsTabByIndex(int tabIndex) {
        detailsSectionTabs
                .get(tabIndex)
                .click();
    }

    public void clickProductDetailsLink() {
        productDetailsLink.click();
    }

    public void clickAddToCartButton() {
        addToCartButton.click();
    }

    public void clickAddToWishlistButton() {
        addToWishlistButton.click();
    }

    public Boolean isAddToCartButtonDisabled() {
        return !addToCartButton.shouldBe(Condition.disabled).isEnabled();
    }

    public Product getProductInformation() {
        SelenideElement defaultImage = productImages.get(0);
        return new Product(brand, productName, defaultImage, price);
    }

    public String getStockIndicatorText(){
        return stockIndicator.getText();
    }

    public void selectColor(int index){
        colorsDropdown.click();
        colorsDropdown.findAll(colorsDropdownOptions).get(index).click();
    }

    public Boolean isImageLinkChanged(String previousImageLink){
        productImages.get(0).shouldNotBe(Condition.attribute("src", previousImageLink));
        return true;
    }
}
