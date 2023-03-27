package that.composites.pop_ups;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.products.ShoppingCartProduct;

import java.util.List;
import java.util.stream.Collectors;

public class CartPopUp {
    @FindBy(xpath = "//*[contains(text(), 'Go to bag')]")
    private SelenideElement goToBagButton;

    @FindBy(xpath = "//*[contains(text(), 'Checkout Now')]")
    private SelenideElement checkoutNowButton;

    @FindBy(xpath = "//cx-cart-modal-login//button[contains(text(), 'Login')]")
    private SelenideElement checkoutLoginButton;

    @FindBy(className = "btn-guest-email")
    private SelenideElement guestCheckoutButton;

    @FindBy(id = "cartEmail")
    private SelenideElement guestEmailInput;

    @FindBy(css = "header cx-cart-item")
    private ElementsCollection productsList;

    public void clickGoToBagButton(){
        goToBagButton.click();
    }

    public void clickCheckoutNowButton(){
        checkoutNowButton.click();
    }

    public void checkoutAsGuest(String guestEmail) {
        clickCheckoutNowButton();
        guestEmailInput.sendKeys(guestEmail);
        guestCheckoutButton.click();
    }

    public void clickCheckoutLoginButton(){
        checkoutLoginButton.click();
    }

    public List<ShoppingCartProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(ShoppingCartProduct::new)
                .collect(Collectors.toList());
    }
}
