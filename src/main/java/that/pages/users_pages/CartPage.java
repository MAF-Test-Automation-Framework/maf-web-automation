package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.products.ShoppingCartProduct;
import that.pages.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends AbstractPage {
    @FindBy(css = "main cx-cart-item")
    private ElementsCollection productsList;

    @FindBy(xpath = "//main//*[contains(text(), 'Checkout')]")
    private SelenideElement checkoutButton;

    @FindBy(className = "cx-summary-total")
    private SelenideElement totalPrice;

    public List<ShoppingCartProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(ShoppingCartProduct::new)
                .collect(Collectors.toList());
    }

    public Boolean isCartEmpty() {
        productsList.shouldHave(CollectionCondition.size(0));
        return true;
    }

    public void clickCheckoutButton(){
        checkoutButton.click();
    }

    public Boolean isTotalPrice(String expectedTotalPrice){
        totalPrice.shouldBe(Condition.partialText(expectedTotalPrice));
        return true;
    }
}
