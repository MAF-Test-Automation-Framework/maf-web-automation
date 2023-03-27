package that.composites.payment_methods;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.AbstractPageComposite;
import that.entities.BankCard;

public class BankCardPayment extends AbstractPageComposite {
    @FindBy(id = "cardNumber")
    private SelenideElement cardNumberInput;

    @FindBy(id = "cardExpiry")
    private SelenideElement cardExpDateInput;

    @FindBy(id = "cardCvc")
    private SelenideElement cardCvvInput;

    @FindBy(className = "proceed-to-payment")
    private SelenideElement proceedToPayment;

    @FindBy(className = "save-card-check-wrapper")
    private SelenideElement saveCardOption;

    public void payWithBankCard(BankCard bankCard) {
        cardNumberInput.sendKeys(bankCard.getCardNumber());
        cardExpDateInput.sendKeys(bankCard.getExpirationDate());
        cardCvvInput.sendKeys(bankCard.getCvv());

        if (saveCardOption.find("[type='checkbox']").isSelected()) {
            saveCardOption.click();
        }

        proceedToPayment.click();
    }
}
