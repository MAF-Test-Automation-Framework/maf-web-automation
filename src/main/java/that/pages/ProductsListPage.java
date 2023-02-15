package that.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.composites.CategoryProduct;
import that.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsListPage extends AbstractPage {

    private By sortButtonOptionsLocator = By.cssSelector("[role='option']");
    @FindBy(tagName = "app-product-list-item")
    private ElementsCollection productsList;

    @FindBy(id = "sorting")
    private SelenideElement sortButton;

    public List<CategoryProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(CategoryProduct::new)
                .collect(Collectors.toList());
    }

    public void sortProductsBy(String option) {
        sortButton.click();
        getElementByText(sortButton.findAll(sortButtonOptionsLocator), option).click();
    }

    public List<Product> convertProductElementsToEntities(List<CategoryProduct> products) {
        return products
                .stream()
                .map(CategoryProduct::getProductInformation)
                .collect(Collectors.toList());
    }
}
