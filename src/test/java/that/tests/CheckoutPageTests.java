package that.tests;

import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.users_pages.CheckoutPage;
import utils.Utils;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.BankCard.TABBY_TEST_BANK_CARD;
import static that.entities.BankCard.TEST_BANK_CARD;
import static that.entities.DeliveryAddress.TEST_ADDRESS;
import static that.entities.User.*;

//TODO: Filter out of stock while bug on P2 env
public class CheckoutPageTests extends AbstractBaseTest{
    final static String SHARE_POINTS_TO_REDEEM = "20";
    final static int TABBY_PAYMENT_PRICE_LIMIT = 2000;

    /**
     * MAF_12: Verify shopping user operation as Guest with bank card payment method
     */
    @Test(groups = {"cartTestsForGuest"})
    public void shopAsGuestTest() {
        productDetailsPage.checkoutAsGuest(SIGN_UP_TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, SIGN_UP_TEST_USER);
        checkoutPage.payWithBankCard(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_13: Verify shopping user operation as Registered user with bank card payment method
     */
    @Test(groups = {"cartTestsForRegisteredUser"})
    public void shopAsRegisteredUserTest() {
        productDetailsPage.checkoutNow();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.payWithBankCard(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(LOGIN_TEST_USER.getEmail());
    }

    /**
     * MAF_23: Verify shopping as Registered user with tabby payment method
     */
    @Test(groups = {"plpTests"})
    public void shopWithTabbyTest() {
        // Tabby payment can be applied only for orders with price less than 2000 AED
        womenShoesPLPage.login(LOGIN_TEST_USER);
        CategoryProduct productForTabbyPayment = womenShoesPLPage
                .getProducts()
                .stream()
                .filter(product -> product.getPrice() < TABBY_PAYMENT_PRICE_LIMIT)
                .findFirst()
                .get();
        productForTabbyPayment.goToProductDetailsPage();

        productDetailsPage = page(ProductDetailsPage.class);
        productDetailsPage.addToCart();

        productDetailsPage.checkoutNow();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.payWithTabby(TABBY_TEST_USER, TABBY_TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(LOGIN_TEST_USER.getEmail());
    }

    /**
     * MAF_21: Sign in with user registered to Share Program, redeem 20 share points, verify discount is applied,
     * Share points are displayed, continue to payment flow, verify it was purchased successfully
     */
    @Test(groups = {"cartTestsForRegisteredUser"}, dependsOnMethods = "shopAsRegisteredUserTest")
    public void redeemShareAmountTest(){
        productDetailsPage.checkoutNow();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        int discountTotalPrice = Utils.getIntegerValueOfPrice(checkoutPage.getTotalPrice()) - Integer.parseInt(SHARE_POINTS_TO_REDEEM)/10;

        checkoutPage.redeemSharePoints(SHARE_POINTS_TO_REDEEM);
        assertThat(checkoutPage.waitTillTotalPriceIs(Utils.getStringValueOfPrice(discountTotalPrice))).isTrue();
        assertThat(checkoutPage.getAppliedSharePoints()).contains(SHARE_POINTS_TO_REDEEM);

        checkoutPage.payWithBankCard(TEST_BANK_CARD);
        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_21: Sign in with user registered to Share Program, redeem 20 share points, verify discount is applied,
     * remove amount, verify there is no discount, Share points aren't displayed
     */
    @Test(groups = {"cartTestsForRegisteredUser", "clearCartTearUp"}, dependsOnGroups = "cartTestsForRegisteredUser")
    public void removeShareAmountTest(){
        productDetailsPage.checkoutNow();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        int totalPrice = Utils.getIntegerValueOfPrice(checkoutPage.getTotalPrice());
        int discountTotalPrice = Utils.getIntegerValueOfPrice(checkoutPage.getTotalPrice()) - Integer.parseInt(SHARE_POINTS_TO_REDEEM)/10;

        checkoutPage.redeemSharePoints(SHARE_POINTS_TO_REDEEM);
        assertThat(checkoutPage.waitTillTotalPriceIs(Utils.getStringValueOfPrice(discountTotalPrice))).isTrue();

        // It'll remove share points
        checkoutPage.clickRedeemRemoveButton();
        assertThat(checkoutPage.waitTillTotalPriceIs(Utils.getStringValueOfPrice(totalPrice))).isTrue();
        assertThat(checkoutPage.getAppliedSharePoints()).isEmpty();
    }
}
