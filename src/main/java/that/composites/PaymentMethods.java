package that.composites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.entities.BankCard;
import that.entities.User;

import static com.codeborne.selenide.Selectors.shadowCss;

public class PaymentMethods extends AbstractPageComposite {

    private By tabbyButtonLocator = shadowCss(".actions", "mafpay-tabby-checkout");

    @FindBy(tagName = "mafpay-checkout")
    private SelenideElement tabbyPaymentSection;

    @FindBy(id = "cardNumber")
    private SelenideElement cardNumberInput;

    @FindBy(id = "cardExpiry")
    private SelenideElement cardExpDateInput;

    @FindBy(id = "cardCvc")
    private SelenideElement cardCvvInput;

    @FindBy(className = "proceed-to-payment")
    private SelenideElement proceedToPayment;

    @FindBy(css = ".nav-tabs li")
    private ElementsCollection paymentMethodTabs;

    @FindBy(css = "[name='email']")
    private SelenideElement tabbyEmailInput;

    @FindBy(css = "[name='phone']")
    private SelenideElement tabbyPhoneInput;

    @FindBy(css = "[data-test='otp.input']")
    private ElementsCollection tabbyOtpInput;

    @FindBy(css = "[name='cardNumber']")
    private SelenideElement tabbyCardNumberInput;

    @FindBy(css = "[name='expiryDate']")
    private SelenideElement tabbyCardExpiryDateInput;

    @FindBy(css = "[name='cvv']")
    private SelenideElement tabbyCvvInput;

    @FindBy(css = "[data-test='pay.submit_pay']")
    private SelenideElement tabbyPayButton;

    @FindBy(xpath = "//button[contains(text(), 'Continue')]")
    private SelenideElement continuePaymentButton;

    public void payWithBankCard(BankCard bankCard) {
        cardNumberInput.sendKeys(bankCard.getCardNumber());
        cardExpDateInput.sendKeys(bankCard.getExpirationDate());
        cardCvvInput.sendKeys(bankCard.getCvv());

        proceedToPayment.click();
    }

    public void clickPaymentMethodTab(String paymentMethod){
        getElementByText(paymentMethodTabs, paymentMethod).click();
    }

    public void clickTabbyButton(){
        tabbyPaymentSection
                .scrollIntoView(true)
                .find(tabbyButtonLocator)
                .click();
    }

    public void fillTabbyUserData(User user){
        clearInputValue(tabbyEmailInput);
        clearInputValue(tabbyPhoneInput);

        tabbyEmailInput.sendKeys(user.getEmail());
        tabbyPhoneInput.sendKeys(user.getPhoneNumber());

        continuePaymentButton.click();
    }

    public void fillTabbyBankCardData(BankCard bankCard){
        int otpInputLength = tabbyOtpInput.shouldBe(CollectionCondition.sizeGreaterThan(0)).size();
        for(int i = 0; i< otpInputLength; i++){
            tabbyOtpInput.get(i).sendKeys(bankCard.getOtp()[i]);
        }
        tabbyCardNumberInput.sendKeys(bankCard.getCardNumber());
        tabbyCardExpiryDateInput.sendKeys(bankCard.getExpirationDate());
        tabbyCvvInput.sendKeys(bankCard.getCvv());
        tabbyPayButton.click();
    }
}
