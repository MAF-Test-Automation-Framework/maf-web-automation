package that.composites;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import that.entities.Product;

import java.util.List;

public class AbstractPageComposite {

    protected SelenideElement getElementByText(List<SelenideElement> listOfItems, String itemName) {
        return listOfItems.stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .get();
    }

    protected Product getProductInformation(SelenideElement brand, SelenideElement productName, SelenideElement imageLink, SelenideElement price) {
        return new Product(brand.getText(), productName.getText(), imageLink.getAttribute("src"), price.getText());
    }

    protected Product getProductInformation(SelenideElement root, By brandLocator, By productNameLocator, By imageLinkLocator, By priceLocator) {
        SelenideElement brand = root.find(brandLocator);
        SelenideElement productName = root.find(productNameLocator);
        SelenideElement imageLink = root.find(imageLinkLocator);
        SelenideElement price = root.find(priceLocator);
        return getProductInformation(brand, productName, imageLink, price);
    }
}
