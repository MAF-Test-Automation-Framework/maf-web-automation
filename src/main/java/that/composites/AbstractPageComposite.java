package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class AbstractPageComposite {

    protected SelenideElement getElementByText(ElementsCollection listOfItems, String itemName) {
        return listOfItems
                .shouldBe(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .get();
    }

    protected SelenideElement getElementContainsText(ElementsCollection listOfItems, String itemName) {
        return listOfItems.stream()
                .filter(item -> item.getText().contains(itemName))
                .findFirst()
                .get();
    }
}
