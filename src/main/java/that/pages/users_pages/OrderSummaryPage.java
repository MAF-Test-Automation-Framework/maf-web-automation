package that.pages.users_pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.products.CheckoutSummaryProduct;
import that.pages.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSummaryPage extends AbstractPage {
    @FindBy(className = "checkout-summary-product")
    private ElementsCollection productsList;

    @FindBy(className = "cx-summary-total")
    private SelenideElement totalPrice;

    @FindBy(tagName = "app-loyalty-summary-row")
    private SelenideElement appliedSharePoints;

    @FindBy(css = "main .input-coupon-code")
    private SelenideElement promoCodeInput;

    @FindBy(css = "main .apply-coupon-button")
    private SelenideElement applyRemovePromoCodeButton;

    @FindBy(css = "main .apply-coupon-error")
    private SelenideElement invalidPromoCodeText;

    public Boolean waitTillTotalPriceIs(String expectedTotalPrice){
        totalPrice.shouldBe(Condition.partialText(expectedTotalPrice));
        return true;
    }

    public String getTotalPrice(){
        return totalPrice.getText();
    }

    public void applyPromoCode(String promoCode){
        promoCodeInput.shouldHave(Condition.empty).sendKeys(promoCode);
        applyRemovePromoCodeButton.click();
    }

    public void removePromoCode(){
        applyRemovePromoCodeButton.click();
    }

    public Boolean isInvalidPromoCodeTextVisible(){
        invalidPromoCodeText.shouldBe(Condition.visible);
        return true;
    }

    public String getAppliedSharePoints(){
        return appliedSharePoints.getText();
    }

    public List<CheckoutSummaryProduct> getSummaryProducts() {
        return productsList
                .shouldHave(CollectionCondition.sizeGreaterThan(0))
                .stream()
                .map(CheckoutSummaryProduct::new)
                .collect(Collectors.toList());
    }
}
