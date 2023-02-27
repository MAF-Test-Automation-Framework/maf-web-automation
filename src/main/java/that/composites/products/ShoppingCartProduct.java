package that.composites.products;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import that.composites.AbstractPageComposite;
import that.entities.Product;

public class ShoppingCartProduct extends AbstractPageComposite {
    private SelenideElement root;
    private By imageLinkLocator = By.cssSelector(".cx-image-container img");
    private By brandLocator = By.className("cx-name");
    private By productNameLocator = By.className("cx-code");
    private By priceLocator = By.cssSelector(".cx-price .cx-value:first-of-type");
    private By quantitySelectLocator = By.className("quantity-select");
    private By removeFromCartButtonLocator = By.cssSelector("[aria-label='Remove Product from Bag']");

    public ShoppingCartProduct(SelenideElement root) {
        this.root = root;
    }

    public Product getProductInformation() {
        return new Product(root, brandLocator, productNameLocator, imageLinkLocator, priceLocator);
    }

    public int getProductCount() {
        Select productQuantitySelect =
                new Select(root.find(quantitySelectLocator));
        String productCount = productQuantitySelect.getFirstSelectedOption().getText();
        return Integer.parseInt(productCount);
    }

    public void selectMaxAvailableProductCount() {
        Select productQuantitySelect =
                new Select(root.find(quantitySelectLocator));
        Integer maxProductCount = productQuantitySelect
                .getOptions()
                .stream()
                .map(option -> Integer.parseInt(option.getText()))
                .max(Integer::compare)
                .get();
        productQuantitySelect.selectByValue(maxProductCount.toString());
    }

    public void selectProductCount(Integer productCount){
        Select productQuantitySelect =
                new Select(root.find(quantitySelectLocator));
        productQuantitySelect.selectByValue(productCount.toString());
    }

    public void removeFromCart() {
        root.find(removeFromCartButtonLocator).click();
    }
}
