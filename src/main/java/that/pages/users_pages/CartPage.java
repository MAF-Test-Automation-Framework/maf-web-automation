package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.products.ShoppingCartProduct;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends OrderSummaryPage {
    @FindBy(css = "main cx-cart-item")
    private ElementsCollection productsList;

    @FindBy(xpath = "//main//*[contains(text(), 'Checkout')]")
    private SelenideElement checkoutButton;

    public List<ShoppingCartProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(ShoppingCartProduct::new)
                .collect(Collectors.toList());
    }

    public Boolean waitTillCartIsEmpty() {
        productsList.shouldHave(CollectionCondition.size(0));
        return true;
    }

    public void checkout(){
        checkoutButton.click();
    }

    public void removeAllProducts(){
        for (int i = getProducts().size(); i>0; i--){
            SelenideElement productToDelete = productsList.shouldBe(CollectionCondition.size(i)).get(0);
            productToDelete.scrollIntoView(false);
            new ShoppingCartProduct(productToDelete).removeFromCart();
        }
        waitTillCartIsEmpty();
    }
}
