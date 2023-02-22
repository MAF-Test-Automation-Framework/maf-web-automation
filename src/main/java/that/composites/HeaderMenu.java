package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class HeaderMenu extends AbstractPageComposite {
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

    @FindBy(xpath = "//button[contains(text(), 'Login')]")
    private SelenideElement loginButton;

    @FindBy(xpath = "//button[contains(text(), 'Logout')]")
    private SelenideElement logoutButton;

    @FindBy(xpath = "//button[contains(text(), 'register now')]")
    private SelenideElement registerNowButton;

    @FindBy(className = "user-details")
    private SelenideElement userDetailsButton;

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

    public void hoverL2MenuCategory(String categoryName) {
        getElementByText(l2MenuCategories, categoryName).hover();
    }

    public void clickSandalsL3MenuCategory() {
        sandalsL3MenuCategory.click();
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
        myAccountButton.hover();
    }

    public void clickUserDetailsButton() {
        userDetailsButton.click();
    }

    public void clickLoginButton() {
        loginButton.shouldBe(Condition.enabled).click();
    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    public void clickRegisterButton() {
        registerNowButton.click();
    }
}
