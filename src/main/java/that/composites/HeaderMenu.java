package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
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

    @FindBy(className = "myAccount-Component")
    private SelenideElement myAccountButton;

    @FindBy(css = ".wishList-Component .tab-icon")
    private SelenideElement wishlistButton;

    @FindBy(className = "ThatUserMenuMiniBag")
    private SelenideElement cartButton;

    @FindBy(id = "//button[contains(text(), 'Login')]")
    private SelenideElement loginButton;

    @FindBy(className = "user-details")
    private SelenideElement userDetailsButton;

    public Boolean isSaleLineVisible() {
        saleLine.shouldBe(Condition.visible);
        return true;
    }

    public Boolean isLogoClickable() {
        logo.shouldBe(Condition.enabled);
        return true;
    }

    public Boolean areTopHeaderItemsClickable() {
        l1MenuCategories.shouldBe(CollectionCondition.allMatch("All elements should be clickable", WebElement::isEnabled));
        return true;
    }

    public Boolean areRightHeaderItemsClickable() {
        searchButton.shouldBe(Condition.enabled);
        wishlistButton.shouldBe(Condition.enabled);
        myAccountButton.shouldBe(Condition.enabled);
        cartButton.shouldBe(Condition.enabled);
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

    public void hoverMyAccountButton(){
        myAccountButton.shouldBe(Condition.visible).hover();
    }
    public void clickUserDetailsButton(){
        userDetailsButton.click();
    }

    public void clickLoginButton(){
        loginButton.click();
    }
}
