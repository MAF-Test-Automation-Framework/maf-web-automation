package that.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;
import that.composites.CategoryProduct;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsListPage extends AbstractPage {

    @FindBy(tagName = "app-product-list-item")
    private ElementsCollection productsList;

    public List<CategoryProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(CategoryProduct::new)
                .collect(Collectors.toList());
    }
}
