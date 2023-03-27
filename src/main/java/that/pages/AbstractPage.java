package that.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import that.composites.AbstractPageComposite;
import that.composites.HeaderMenu;
import that.composites.forms.SignInSignUpUserForm;
import that.composites.products.ShoppingCartProduct;
import that.entities.User;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class AbstractPage extends AbstractPageComposite {
    HeaderMenu headerMenu;
    SignInSignUpUserForm userDataForm;

    @FindBy(className = "cookie-warning-forced-close-button")
    private SelenideElement cookieNotificationButton;

    @FindBy(className = "breadcrumb-wrapper")
    private SelenideElement breadCrumb;

    @FindBy(css = ".search-box-wrapper input")
    private SelenideElement productSearchInput;

    public AbstractPage() {
        headerMenu = page(HeaderMenu.class);
        userDataForm = page(SignInSignUpUserForm.class);
    }

    public void closeCookieNotification() {
        cookieNotificationButton.click();
    }

    public Boolean waitTillTitleContains(String titlePart) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.titleContains(titlePart));
        return true;
    }

    public Boolean areAllHeaderMenuItemsClickableOrVisible() {
        return headerMenu.areRightHeaderItemsClickable()
                && headerMenu.areTopHeaderItemsClickable()
                && headerMenu.isLogoClickable()
                && headerMenu.isSaleLineVisible();
    }

    public void clickHeaderL1Category(String categoryName) {
        headerMenu.clickL1MenuCategory(categoryName);
    }

    public void clickHeaderL2Category(String l1CategoryName, String l2CategoryName) {
        headerMenu.hoverL1MenuCategory(l1CategoryName);
        headerMenu.clickL2MenuCategory(l2CategoryName);
    }

    public void clickHeaderL3Category(String l1CategoryName, String l2CategoryName, String l3CategoryName) {
        headerMenu.hoverL1MenuCategory(l1CategoryName);
        SelenideElement l2CategoryElement = headerMenu.hoverL2MenuCategory(l2CategoryName);
        headerMenu.clickL3MenuCategory(l2CategoryElement, l3CategoryName);
    }

    public String getBreadCrumbText() {
        return breadCrumb.getText();
    }

    public void searchProduct(String searchedProductName) {
        headerMenu.clickSearchButton();
        productSearchInput.sendKeys(searchedProductName, Keys.ENTER);
    }

    public List<ShoppingCartProduct> getCartProductsFromHeader() {
        return headerMenu.getCartProducts();
    }

    public void goToCartPage() {
        headerMenu.clickGoToBagButton();
    }

    public void goToWishlistPage() {
        headerMenu.clickWishlistButton();
    }

    public void login(User user) {
        headerMenu.clickLoginButton();
        fillLoginForm(user);
    }

    public void checkoutAsLoggedInUser(User user){
        headerMenu.checkoutAsLoggedInUser();
        fillLoginForm(user);
    }

    public void fillLoginForm(User user){
        userDataForm.fillEmail(user.getEmail());
        userDataForm.fillPassword(user.getPassword());
        userDataForm.clickSignInButton();
    }

    public void goToAccountPage() {
        headerMenu.clickUserDetailsButton();
    }

    public void checkoutNow() {
        headerMenu.clickCheckoutNowButton();
    }

    public void logout() {
        headerMenu.clickLogoutButton();
    }

    public void signUp(User user) {
        headerMenu.clickRegisterButton();

        userDataForm.selectTitle(user.getTitle());
        userDataForm.fillDateOfBirth(user.getYear(), user.getMonth(), user.getDay());
        userDataForm.fillNationality(user.getNationality());
        userDataForm.fillEmail(user.getEmail());
        userDataForm.fillPassword(user.getPassword());
        userDataForm.fillName(user.getFirstName(), user.getLastName());
        userDataForm.selectCountryCode(user.getPhoneCountryCode());
        userDataForm.fillPhoneNumber(user.getPhoneNumber());

        userDataForm.clickSignUpButton();
        userDataForm.clickEmailCheckConfirmationBtn();
        userDataForm.clickSignUpButton();
    }

    public void checkoutAsGuest(String guestEmail) {
        headerMenu.checkoutAsGuest(guestEmail);
    }

    public String getUserWelcomeText(){
        return headerMenu.getUserDetailsButtonText();
    }
}
