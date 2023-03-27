package that.pages.users_pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.pages.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersPage extends AbstractPage {
    private By productNameLocator = By.className("name");

    @FindBy(xpath = "//*[contains(text(), 'Order #')]/following-sibling::*[1]")
    SelenideElement orderNumber;

    @FindBy(className = "product")
    ElementsCollection products;

    @FindBy(xpath = "//*[contains(text(), 'Total')]/following-sibling::div[1]")
    SelenideElement totalPrice;

    public String getOrderNumberText(){
        return orderNumber.getText();
    }

    public List<String> getProductNamesText(){
        return products
                .stream()
                .map(product -> product.find(productNameLocator).getText())
                .collect(Collectors.toList());
    }

    public String getTotalPriceText(){
        return totalPrice.getText();
    }
}
