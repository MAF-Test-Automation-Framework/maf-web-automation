package that.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.entities.BankCard;
import that.entities.DeliveryAddress;
import that.entities.User;

public class CheckoutPage extends AbstractPage {
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

    @FindBy(id = "cardNumber")
    private SelenideElement cardNumberInput;

    @FindBy(id = "cardExpiry")
    private SelenideElement cardExpDateInput;

    @FindBy(id = "cardCvc")
    private SelenideElement cardCvvInput;

    @FindBy(className = "proceed-to-payment")
    private SelenideElement proceedToPayment;

    @FindBy(className = "order-confirmation-thanks")
    private SelenideElement orderConfirmation;

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

        streetNameInput.sendKeys(deliveryAddress.getStreet());
        buildingInput.sendKeys(deliveryAddress.getBuilding());
        flatNumberInput.sendKeys(deliveryAddress.getFlat());
        floorNumberInput.sendKeys(deliveryAddress.getFloor());
    }

    public void fillUserForm(User user) {
        firstNameInput.sendKeys(user.getFirstName());
        lastNameInput.sendKeys(user.getLastName());
        phoneNumberInput.sendKeys(user.getPhoneNumber());
    }

    public void clickContinueToPaymentButton() {
        continueToPaymentButton.shouldBe(Condition.enabled).click();
    }

    public void fillPaymentInfo(BankCard bankCard) {
        cardNumberInput.sendKeys(bankCard.getCardNumber());
        cardExpDateInput.sendKeys(bankCard.getExpirationDate());
        cardCvvInput.sendKeys(bankCard.getCvv());

        proceedToPayment.click();
    }

    public String getOrderConfirmationText() {
        return orderConfirmation.getText();
    }
}
