package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractPageComposite {

    protected SelenideElement getElementByText(ElementsCollection listOfItems, String itemName) {
        return waitTillAllElementsAreVisible(listOfItems)
                .stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .get();
    }

    protected SelenideElement getElementContainsText(ElementsCollection listOfItems, String itemName) {
        return waitTillAllElementsAreVisible(listOfItems)
                .stream()
                .filter(item -> item.getText().contains(itemName))
                .findFirst()
                .get();
    }

    protected List<String> getAllItemsText(ElementsCollection listOfItems) {
        return waitTillAllElementsAreVisible(listOfItems)
                .shouldBe(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList());
    }

    protected ElementsCollection waitTillAllElementsAreVisible(ElementsCollection listOfItems){
        return listOfItems
                .shouldBe(CollectionCondition.allMatch("All elements should be visible", WebElement::isDisplayed));
    }
}
