package that.pages;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;
import that.composites.WishlistProduct;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistPage extends AbstractPage{
    @FindBy(css = "main .item")
    private ElementsCollection productsList;

    public List<WishlistProduct> getProducts() {
        return productsList
                .stream()
                .map(WishlistProduct::new)
                .collect(Collectors.toList());
    }
}
