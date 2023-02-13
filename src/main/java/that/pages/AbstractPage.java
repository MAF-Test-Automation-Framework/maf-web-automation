package that.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import that.composites.AbstractPageComposite;
import that.composites.HeaderMenu;
import that.composites.ShoppingCartProduct;
import that.composites.WishlistProduct;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractPage extends AbstractPageComposite {
    HeaderMenu headerMenu;
    @FindBy(className = "ab-close-button")
    private WebElement notificationTestCloseButton;

    @FindBy(className = "cookie-warning-forced-close-button")
    private WebElement cookieNotificationButton;

    @FindBy(className = "breadcrumb-wrapper")
    private WebElement breadCrumb;

    @FindBy(css = ".search-box-wrapper input")
    private WebElement productSearchInput;

    @FindBy(css = ".my-list .description")
    private List<WebElement> foundProductsDescList;

    @FindBy(xpath = "//*[contains(text(), 'Go to bag')]")
    private WebElement goToBagCartPopUpButton;

    @FindBy(css = "header cx-cart-item")
    private List<WebElement> cartProductsList;

    @FindBy(css = "header .item")
    private List<WebElement> wishlistProductList;

    @FindBy(className = "go-to-link")
    private WebElement goToWishlistButton;

    public AbstractPage(WebDriver driver) {
        super(driver);
        headerMenu = new HeaderMenu(driver);
    }

    public void clickNotificationTestCloseButton() {
        clickItem(notificationTestCloseButton);
    }

    public void clickCookieNotificationCloseButton() {
        clickItem(cookieNotificationButton);
    }

    public Boolean hasTitleCorrectName(String titleName) {
        return waitForTitle(titleName);
    }

    public Boolean doesTitleContainPart(String titlePart) {
        return waitForTitleContains(titlePart);
    }

    public Boolean areAllHeaderMenuItemsClickableOrVisible() {
        return headerMenu.areRightHeaderItemsClickable()
                && headerMenu.areTopHeaderItemsClickable()
                && headerMenu.isLogoClickable()
                && headerMenu.isSaleLineVisible();
    }

    public void clickHeaderL1Category(String categoryName){
        headerMenu.clickL1MenuCategory(categoryName);
    }

    public void clickHeaderL2Category(String l1CategoryName, String l2CategoryName){
        headerMenu.hoverL1MenuCategory(l1CategoryName);
        headerMenu.clickL2MenuCategory(l2CategoryName);
    }

    // TODO: ???
    public void clickHeaderSandalsL3Category(){
        headerMenu.hoverL1MenuCategory("WOMEN");
        headerMenu.hoverL2MenuCategory("SHOES");
        headerMenu.clickSandalsL3MenuCategory();
    }

    public String getBreadCrumbText() {
        return getItemText(breadCrumb);
    }

    public void searchProductFromHeader(String searchedProductName){
        headerMenu.clickSearchButton();
        wait.until(ExpectedConditions.visibilityOf(productSearchInput)).sendKeys(searchedProductName, Keys.ENTER);
    }

    public List<ShoppingCartProduct> getCartPopUpProductsFromHeader(){
        return wait
                .until(ExpectedConditions.visibilityOfAllElements(cartProductsList))
                .stream()
                .map(elem -> new ShoppingCartProduct(driver, elem))
                .collect(Collectors.toList());
    }

    public void clickGoToBagHeaderCartPopUpButton(){
        headerMenu.hoverCartButton();
        clickItem(goToBagCartPopUpButton);
    }

    public void hoverHeaderWishlistButton(){
        headerMenu.hoverWishlistButton();
    }

    public List<WishlistProduct> getWishlistPopUpProductsFromHeader(){
        return wait
                .until(ExpectedConditions.visibilityOfAllElements(wishlistProductList))
                .stream()
                .map(elem -> new WishlistProduct(driver, elem))
                .collect(Collectors.toList());
    }

    public void clickGoToWishlistHeaderPopUpButton(){
        headerMenu.hoverWishlistButton();
        clickItem(goToWishlistButton);
    }
}
