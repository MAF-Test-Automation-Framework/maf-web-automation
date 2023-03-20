package that.pages.users_pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.pages.AbstractPage;

public class OrderSummaryPage extends AbstractPage {
    @FindBy(className = "cx-summary-total")
    private SelenideElement totalPrice;

    @FindBy(tagName = "app-loyalty-summary-row")
    private SelenideElement appliedSharePoints;

    @FindBy(css = "main .input-coupon-code")
    private SelenideElement promoCodeInput;

    @FindBy(css = "main .apply-coupon-button")
    private SelenideElement applyPromoCodeButton;

    @FindBy(css = "main .apply-coupon-error")
    private SelenideElement invalidPromoCodeText;

    public Boolean isTotalPrice(String expectedTotalPrice){
        totalPrice.shouldBe(Condition.partialText(expectedTotalPrice));
        return true;
    }

    public String getTotalPrice(){
        return totalPrice.getText();
    }

    public void applyPromoCode(String promoCode){
        promoCodeInput.shouldHave(Condition.empty).sendKeys(promoCode);
        applyPromoCodeButton.click();
    }

    public Boolean isInvalidPromoCodeTextVisible(){
        invalidPromoCodeText.shouldBe(Condition.visible);
        return true;
    }

    public String getAppliedSharePoints(){
        return appliedSharePoints.getText();
    }
}
