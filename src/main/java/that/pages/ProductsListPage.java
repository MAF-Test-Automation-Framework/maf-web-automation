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

import static utils.Utils.getIntegerValueOfPrice;

public class ProductsListPage extends AbstractPage {

    private By sortButtonOptionsLocator = By.cssSelector("[role='option']");
    private By l2FilterCategoriesLocator = By.className("checkbox");
    private By l2FilterCategoriesProductCountLocator = By.className("product-count");
    @FindBy(tagName = "app-product-list-item")
    private ElementsCollection productsList;

    @FindBy(id = "sorting")
    private SelenideElement sortButton;

    @FindBy(css = ".sort-filter-box .filter")
    private SelenideElement filterButton;

    @FindBy(xpath = "//*[@class = 'filtersUpperContainer']")
    private ElementsCollection l1FilterCategories;

    public List<CategoryProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(CategoryProduct::new)
                .collect(Collectors.toList());
    }

    public List<CategoryProduct> getProductsMoreThanOrEqual(int numberOfProducts) {
        int productsListSize = productsList.size();
        while (productsListSize < numberOfProducts) {
            productsList.get(productsListSize - 1).scrollIntoView(false);
            productsListSize = productsList.shouldBe(CollectionCondition.sizeGreaterThan(productsListSize)).size();
        }
        return getProducts();
    }

    public void sortProductsBy(String option) {
        sortButton.click();
        getElementByText(sortButton.findAll(sortButtonOptionsLocator), option).click();
    }


    /**
     * @return number of filtered products
     */
    public int filterProducts(String l1Category, String l2Category) {
        filterButton.click();

        SelenideElement l1CategoryElement = getElementByText(l1FilterCategories, l1Category);
        l1CategoryElement.click();

        SelenideElement l2CategoryElement = getElementContainsText(l1CategoryElement.findAll(l2FilterCategoriesLocator), l2Category);
        String productCount = l2CategoryElement.find(l2FilterCategoriesProductCountLocator).getText();
        l2CategoryElement.click();

        l1CategoryElement.click();
        filterButton.click();

        return getIntegerValueOfPrice(productCount);
    }

    public List<Product> convertProductElementsToEntities(List<CategoryProduct> products) {
        return products
                .stream()
                .map(CategoryProduct::getProductInformation)
                .collect(Collectors.toList());
    }
}
