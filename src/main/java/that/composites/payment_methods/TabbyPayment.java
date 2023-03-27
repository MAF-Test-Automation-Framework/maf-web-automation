package that.composites.payment_methods;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import that.composites.AbstractPageComposite;
import that.entities.BankCard;
import that.entities.User;

import static com.codeborne.selenide.Selectors.shadowCss;

public class TabbyPayment extends AbstractPageComposite {
    private By tabbyOptionButtonLocator = shadowCss(".actions", "mafpay-tabby-checkout");

    @FindBy(tagName = "mafpay-checkout")
    private SelenideElement tabbyOptionSection;

    @FindBy(css = "[name='email']")
    private SelenideElement emailInput;

    @FindBy(css = "[name='phone']")
    private SelenideElement phoneInput;

    @FindBy(css = "[data-test='otp.input']")
    private ElementsCollection otpInput;

    @FindBy(css = "[name='cardNumber']")
    private SelenideElement cardNumberInput;

    @FindBy(css = "[name='expiryDate']")
    private SelenideElement cardExpiryDateInput;

    @FindBy(css = "[name='cvv']")
    private SelenideElement cvvInput;

    @FindBy(css = "[data-test='pay.submit_pay']")
    private SelenideElement payButton;

    @FindBy(xpath = "//button[contains(text(), 'Continue')]")
    private SelenideElement continuePaymentButton;

    public void fillUserData(User user) {
        clearInputValue(emailInput);
        clearInputValue(phoneInput);

        emailInput.sendKeys(user.getEmail());
        phoneInput.sendKeys(user.getPhoneNumber());

        continuePaymentButton.click();
    }

    public void fillBankCardData(BankCard bankCard) {
        int otpInputLength = otpInput.shouldBe(CollectionCondition.sizeGreaterThan(0)).size();
        for (int i = 0; i < otpInputLength; i++) {
            otpInput.get(i).sendKeys(bankCard.getOtp()[i]);
        }
        cardNumberInput.sendKeys(bankCard.getCardNumber());
        cardExpiryDateInput.sendKeys(bankCard.getExpirationDate());
        cvvInput.sendKeys(bankCard.getCvv());
        payButton.click();
    }

    public void selectTabbyPayment() {
        tabbyOptionSection
                .scrollIntoView(true)
                .find(tabbyOptionButtonLocator)
                .click();
    }
}
