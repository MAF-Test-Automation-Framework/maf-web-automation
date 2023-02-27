package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;
import that.pages.AbstractPage;

import java.util.List;

public class AccountPage extends AbstractPage {
    @FindBy(className = "headline")
    private ElementsCollection sectionHeaders;

    @FindBy(xpath = "//a[contains(@class, 'account-link')]")
    private ElementsCollection sections;

    public void clickSection(String section){
        getElementByText(sections, section).click();
    }

    public List<String> getSectionNames(){
        return getAllItemsText(sections);
    }

    public Boolean isSectionHeadersContains(String ... titles){
        sectionHeaders.shouldHave(CollectionCondition.texts(titles));
        return true;
    }
}
