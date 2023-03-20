package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.composites.PaymentMethods;
import that.composites.products.CheckoutSummaryProduct;
import that.entities.BankCard;
import that.entities.DeliveryAddress;
import that.entities.User;
import that.pages.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.page;

public class CheckoutPage extends OrderSummaryPage {
    public final static String BANK_CARD_PAYMENT_METHOD = "CARD PAYMENT";
    public final static String TABBY_PAYMENT_METHOD = "INTEREST FREE INSTALLMENTS";
    private By dropdownOptionsLocator = By.className("ng-option");
    private PaymentMethods paymentMethods;

    @FindBy(className = "checkout-summary-product")
    private ElementsCollection productsList;

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

    @FindBy(className = "order-confirmation-thanks")
    private SelenideElement orderConfirmation;

    public CheckoutPage() {
        paymentMethods = page(PaymentMethods.class);
    }

    public void fillFormForGuest(DeliveryAddress deliveryAddress, User user){
        fillUserForm(user);
        fillFormForRegisteredUser(deliveryAddress);
    }

    public void fillFormForRegisteredUser(DeliveryAddress deliveryAddress){
        fillDeliveryAddress(deliveryAddress);
        clickContinueToPaymentButton();
    }

    private void fillDeliveryAddress(DeliveryAddress deliveryAddress) {
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

    private void fillUserForm(User user) {
        firstNameInput.setValue(user.getFirstName());
        lastNameInput.setValue(user.getLastName());
        phoneNumberInput.setValue(user.getPhoneNumber());
    }

    private void clickContinueToPaymentButton() {
        continueToPaymentButton.shouldBe(Condition.enabled).click();
    }

    public String getOrderConfirmationText() {
        return orderConfirmation.getText();
    }

    public List<CheckoutSummaryProduct> getProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(CheckoutSummaryProduct::new)
                .collect(Collectors.toList());
    }

    public void payWithBankCard(BankCard bankCard){
        paymentMethods.clickPaymentMethodTab(BANK_CARD_PAYMENT_METHOD);
        paymentMethods.payWithBankCard(bankCard);
    }

    public void payWithTabby(User user, BankCard bankCard){
        paymentMethods.clickPaymentMethodTab(TABBY_PAYMENT_METHOD);
        paymentMethods.clickTabbyButton();

        paymentMethods.fillTabbyUserData(user);
        paymentMethods.fillTabbyBankCardData(bankCard);
    }

    public void redeemSharePoints(String sharePoints){
        paymentMethods.checkUseSharePointsCheckbox();
        paymentMethods.enterSharePoints(sharePoints);
        paymentMethods.clickRedeemRemoveButton();
    }

    public void clickRedeemRemoveButton(){
        paymentMethods.clickRedeemRemoveButton();
    }
}
