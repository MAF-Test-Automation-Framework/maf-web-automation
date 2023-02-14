package that.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;
import that.composites.ShoppingCartProduct;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends AbstractPage {
    @FindBy(css = "main cx-cart-item")
    private ElementsCollection productsList;

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
}
