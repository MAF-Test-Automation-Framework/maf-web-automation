package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.composites.pop_ups.AccountPopUp;
import that.composites.pop_ups.CartPopUp;
import that.composites.products.ShoppingCartProduct;

import java.util.List;

import static com.codeborne.selenide.Selenide.page;

public class HeaderMenu extends AbstractPageComposite {
    AccountPopUp accountPopUp;
    CartPopUp cartPopUp;
    @FindBy(className = "SiteLinks")
    private SelenideElement saleLine;

    @FindBy(css = "[position='SiteLogo']")
    private SelenideElement logo;

    @FindBy(className = "menu-item")
    private ElementsCollection l1MenuCategories;

    @FindBy(css = "#WomenNavNode > li")
    private ElementsCollection l2MenuCategories;

    @FindBy(className = "sandals")
    private SelenideElement sandalsL3MenuCategory;

    @FindBy(css = ".HeaderSearchBox .tab-icon")
    private SelenideElement searchButton;

    @FindBy(css = ".myAccount-Component")
    private SelenideElement myAccountButton;

    @FindBy(css = ".wishList-Component .tab-icon")
    private SelenideElement wishlistButton;

    @FindBy(className = "ThatUserMenuMiniBag")
    private SelenideElement cartButton;

    public HeaderMenu() {
        accountPopUp = page(AccountPopUp.class);
        cartPopUp = page(CartPopUp.class);
    }

    public Boolean isSaleLineVisible() {
        saleLine.shouldBe(Condition.visible);
        return true;
    }

    public Boolean isLogoClickable() {
        logo.shouldBe(Condition.enabled, Condition.visible);
        return true;
    }

    public Boolean areTopHeaderItemsClickable() {
        l1MenuCategories.shouldBe(CollectionCondition.allMatch
                (
                        "All elements should be clickable",
                        element -> element.isEnabled() && element.isDisplayed()
                ));
        return true;
    }

    public Boolean areRightHeaderItemsClickable() {
        searchButton.shouldBe(Condition.enabled, Condition.visible);
        wishlistButton.shouldBe(Condition.enabled, Condition.visible);
        myAccountButton.shouldBe(Condition.enabled, Condition.visible);
        cartButton.shouldBe(Condition.enabled, Condition.visible);
        return true;
    }

    public void clickL1MenuCategory(String categoryName) {
        getElementByText(l1MenuCategories, categoryName).click();
    }

    public void hoverL1MenuCategory(String categoryName) {
        getElementByText(l1MenuCategories, categoryName).hover();
    }

    public void clickL2MenuCategory(String categoryName) {
        getElementByText(l2MenuCategories, categoryName).click();
    }

    public SelenideElement hoverL2MenuCategory(String categoryName) {
        SelenideElement l2CategoryElement = getElementByText(l2MenuCategories, categoryName);
        l2CategoryElement.hover();
        return l2CategoryElement;
    }

    public void clickL3MenuCategory(SelenideElement l2CategoryElement, String l3CategoryName) {
        getElementContainsText(l2CategoryElement.findAll(By.cssSelector("li")), l3CategoryName).click();
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    public void hoverCartButton() {
        cartButton.hover();
    }

    public void clickWishlistButton() {
        wishlistButton.click();
    }

    public void hoverMyAccountButton() {
        areRightHeaderItemsClickable();
        myAccountButton.shouldBe(Condition.enabled).hover();
    }

    public void clickLoginButton(){
        hoverMyAccountButton();
        accountPopUp.clickLoginButton();
    }

    public void clickUserDetailsButton(){
        hoverMyAccountButton();
        accountPopUp.clickUserDetailsButton();
    }

    public void clickLogoutButton(){
        hoverMyAccountButton();
        accountPopUp.clickLogoutButton();
    }

    public void clickRegisterButton(){
        hoverMyAccountButton();
        accountPopUp.clickRegisterButton();
    }

    public String getUserDetailsButtonText(){
        hoverMyAccountButton();
        return accountPopUp.getUserDetailsButtonText();
    }

    public void clickGoToBagButton(){
        hoverCartButton();
        cartPopUp.clickGoToBagButton();
    }

    public void clickCheckoutNowButton(){
        hoverCartButton();
        cartPopUp.clickCheckoutNowButton();
    }

    public void checkoutAsLoggedInUser(){
        clickCheckoutNowButton();
        cartPopUp.clickCheckoutLoginButton();
    }

    public List<ShoppingCartProduct> getCartProducts() {
        return cartPopUp.getProducts();
    }

    public void checkoutAsGuest(String guestEmail){
        cartPopUp.checkoutAsGuest(guestEmail);
    }
}
