package that.composites;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HeaderMenu extends AbstractPageComposite{
    @FindBy(className = "SiteLinks")
    private WebElement saleLine;

    @FindBy(css="[position='SiteLogo']")
    private WebElement logo;

    @FindBy(className = "menu-item")
    private List<WebElement> l1MenuCategories;

    @FindBy(css = "#WomenNavNode > li")
    private List<WebElement> l2MenuCategories;

    @FindBy(className = "sandals")
    private WebElement sandalsL3MenuCategory;

    @FindBy(css = ".HeaderSearchBox .tab-icon")
    private WebElement searchButton;

    @FindBy(className = "myAccount-Component")
    private WebElement myAccountButton;

    @FindBy(className = "wishList-Component")
    private WebElement wishlistButton;

    @FindBy(className = "ThatUserMenuMiniBag")
    private WebElement cartButton;

    public HeaderMenu(WebDriver driver) {
        super(driver);
    }

    public Boolean isSaleLineVisible(){
        return saleLine.isDisplayed();
    }
    public Boolean isLogoClickable(){
        return isItemClickable(logo);
    }
    public Boolean areTopHeaderItemsClickable(){
        return areListItemsClickable(l1MenuCategories);
    }
    public Boolean areRightHeaderItemsClickable(){
        return isItemClickable(searchButton)
                && isItemClickable(wishlistButton)
                && isItemClickable(myAccountButton)
                && isItemClickable(cartButton);
    }
    public void clickL1MenuCategory(String categoryName){
        clickListItem(l1MenuCategories, categoryName);
    }
    public void hoverL1MenuCategory(String categoryName){
        moveToListItem(l1MenuCategories, categoryName);
    }
    public void clickL2MenuCategory(String categoryName){
        clickListItem(l2MenuCategories, categoryName);
    }
    public void hoverL2MenuCategory(String categoryName){
        moveToListItem(l2MenuCategories, categoryName);
    }
    public void clickSandalsL3MenuCategory(){
        clickItem(sandalsL3MenuCategory);
    }
    public void clickSearchButton(){
        clickItem(searchButton);
    }
    public void hoverCartButton(){
        moveToItem(cartButton);
    }
    public void hoverWishlistButton(){
        moveToItem(wishlistButton);
    }
}
