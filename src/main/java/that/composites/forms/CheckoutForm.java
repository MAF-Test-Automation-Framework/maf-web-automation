package that.composites.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.composites.AbstractPageComposite;
import that.entities.DeliveryAddress;
import that.entities.User;

public class CheckoutForm extends AbstractPageComposite {
    //Universal locator for any dropdown options
    private By dropdownOptionsLocator = By.className("ng-option");

    @FindBy(css = "[name='firstName']")
    private SelenideElement firstNameInput;

    @FindBy(css = "[name='lastName']")
    private SelenideElement lastNameInput;

    @FindBy(id = "cellphone")
    private SelenideElement phoneNumberInput;

    @FindBy(xpath = "//*[label[contains(text(), 'Country')]]//ng-select")
    private SelenideElement countryDropdown;

    @FindBy(xpath = "//*[label[contains(text(), 'City')]]//ng-select")
    private SelenideElement cityDropdown;

    @FindBy(xpath = "//*[label[contains(text(), 'Area')]]//ng-select")
    private SelenideElement areaDropdown;

    @FindBy(css = "[name='streetname']")
    private SelenideElement streetNameInput;

    @FindBy(css = "[name='building']")
    private SelenideElement buildingInput;

    @FindBy(css = "[name='appartment']")
    private SelenideElement flatNumberInput;

    @FindBy(css = "[name='floorNumber']")
    private SelenideElement floorNumberInput;

    @FindBy(className = "checkout-form-controls")
    private SelenideElement continueToPaymentButton;

    public void fillDeliveryAddress(DeliveryAddress deliveryAddress) {
        countryDropdown.click();
        ElementsCollection countryOptions = countryDropdown.findAll(dropdownOptionsLocator);
        getElementByText(countryOptions, deliveryAddress.getCountry()).click();

        cityDropdown.click();
        ElementsCollection cityOptions = cityDropdown.findAll(dropdownOptionsLocator);
        getElementByText(cityOptions, deliveryAddress.getCity()).click();

        areaDropdown.click();
        ElementsCollection areaOptions = areaDropdown.findAll(dropdownOptionsLocator);
        getElementByText(areaOptions, deliveryAddress.getArea()).click();

        streetNameInput.setValue(deliveryAddress.getStreet());
        buildingInput.setValue(deliveryAddress.getBuilding());
        flatNumberInput.setValue(deliveryAddress.getFlat());
        floorNumberInput.setValue(deliveryAddress.getFloor());
    }

    public void fillUserData(User user) {
        firstNameInput.setValue(user.getFirstName());
        lastNameInput.setValue(user.getLastName());
        phoneNumberInput.setValue(user.getPhoneNumber());
    }

    public void continueToPayment() {
        continueToPaymentButton.shouldBe(Condition.enabled).click();
    }
}
