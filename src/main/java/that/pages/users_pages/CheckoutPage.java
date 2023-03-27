package that.pages.users_pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.forms.CheckoutForm;
import that.composites.payment_methods.BankCardPayment;
import that.composites.payment_methods.TabbyPayment;
import that.entities.BankCard;
import that.entities.DeliveryAddress;
import that.entities.User;

import static com.codeborne.selenide.Selenide.page;

public class CheckoutPage extends OrderSummaryPage {
    public final static String BANK_CARD_PAYMENT_METHOD = "CARD PAYMENT";
    public final static String INSTALLMENTS_PAYMENT_METHOD = "INTEREST FREE INSTALLMENTS";
    private BankCardPayment bankCardPayment;
    private TabbyPayment tabbyPayment;
    private CheckoutForm checkoutForm;

    @FindBy(css = ".nav-tabs li")
    private ElementsCollection paymentMethodTabs;

    @FindBy(className = "order-confirmation-thanks")
    private SelenideElement orderConfirmation;

    @FindBy(css = "[for='useSharePoints']")
    private SelenideElement useSharePointsCheckbox;

    @FindBy(css = "[name='points']")
    private SelenideElement sharePointsInput;

    @FindBy(className = "shareActions")
    private SelenideElement redeemRemoveButton;

    public CheckoutPage() {
        tabbyPayment = page(TabbyPayment.class);
        bankCardPayment = page(BankCardPayment.class);
        checkoutForm = page(CheckoutForm.class);
    }

    public void fillFormForGuest(DeliveryAddress deliveryAddress, User user){
        checkoutForm.fillUserData(user);
        fillFormForRegisteredUser(deliveryAddress);
    }

    public void fillFormForRegisteredUser(DeliveryAddress deliveryAddress){
        checkoutForm.fillDeliveryAddress(deliveryAddress);
        checkoutForm.continueToPayment();
    }

    public String getOrderConfirmationText() {
        return orderConfirmation.getText();
    }

    public void payWithBankCard(BankCard bankCard){
        clickPaymentMethodTab(BANK_CARD_PAYMENT_METHOD);
        bankCardPayment.payWithBankCard(bankCard);
    }

    public void payWithTabby(User user, BankCard bankCard){
        clickPaymentMethodTab(INSTALLMENTS_PAYMENT_METHOD);
        tabbyPayment.selectTabbyPayment();

        tabbyPayment.fillUserData(user);
        tabbyPayment.fillBankCardData(bankCard);
    }

    public void redeemSharePoints(String sharePoints){
        useSharePointsCheckbox.click();
        sharePointsInput.sendKeys(sharePoints);
        clickRedeemRemoveButton();
    }

    public void clickPaymentMethodTab(String paymentMethod){
        getElementByText(paymentMethodTabs, paymentMethod).click();
    }

    public void clickRedeemRemoveButton(){
        redeemRemoveButton.click();
    }
}
