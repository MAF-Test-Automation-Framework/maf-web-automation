package that.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import that.composites.CategoryProduct;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsListPage extends AbstractPage {

    @FindBy(tagName = "app-product-list-item")
    private List<WebElement> productsList;

    public ProductsListPage(WebDriver driver) {
        super(driver);
    }

    public List<CategoryProduct> getProducts(){
        return productsList
                .stream()
                .map(elem -> new CategoryProduct(driver, elem))
                .collect(Collectors.toList());
    }
}
