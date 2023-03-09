package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.pages.AbstractPage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AccountPage extends AbstractPage {
    private By viewOrderButtonLocator = By.className("link-view-order");

    @FindBy(className = "headline")
    private ElementsCollection sectionHeaders;

    @FindBy(xpath = "//a[contains(@class, 'account-link')]")
    private ElementsCollection sections;

    @FindBy(className = "order-wrapper")
    private ElementsCollection orders;

    @FindBy(className = "btn-invoice")
    private SelenideElement invoiceButton;

    public void clickSection(String section) {
        getElementByText(sections, section).click();
    }

    public List<String> getSectionNames() {
        return getAllItemsText(sections);
    }

    public Boolean isSectionHeadersContains(String... titles) {
        sectionHeaders.shouldHave(CollectionCondition.texts(titles));
        return true;
    }

    public File downloadInvoiceForOrder(int index){
        try {
            orders.get(index).find(viewOrderButtonLocator).click();
            return invoiceButton.download();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
