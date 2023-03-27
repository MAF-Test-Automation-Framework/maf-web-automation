package that.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.CheckoutSummaryProduct;
import that.composites.products.ShoppingCartProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.users_pages.CartPage;
import that.pages.users_pages.CheckoutPage;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.BankCard.TEST_BANK_CARD;
import static that.entities.DeliveryAddress.TEST_ADDRESS;
import static that.entities.User.LOGIN_TEST_USER;
import static that.entities.User.SIGN_UP_TEST_USER;
import static that.test_data.PageTitlesAndBreadCrumbs.CART_PAGE_TITLE;
import static utils.Utils.getStringValueOfPrice;

//TODO: Filter out of stock while bug on P2 env 3 tests
public class CartPageTests extends AbstractBaseTest{

    final static String INVALID_PROMO_CODE = "INVALID";
    final static String VALID_PROMO_CODE = "THATAUTO"; //50 AED discount

    @AfterMethod(onlyForGroups = "clearCartAndPromoCodeTearUp")
    public void promoCodeTearUp(){
        CartPage cartPage = page(CartPage.class);
        cartPage.removePromoCode();
        cartPage.removeAllProducts();
        closeWebDriver();
    }

    /**
     * MAF_20: Go to Product Details page, add product to cart,
     * verify Product from Cart pop up windows is the same as PDP Product, max count,
     * click 'Go to bag' button, verify user is redirected to the correct page
     */
    @Test(groups = {"plpTests"})
    public void addToBagTest() {
        CategoryProduct product = womenShoesPLPage.getProducts().get(0);
        product.goToProductDetailsPage();

        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = productDetailsPage.getInformation();
        productDetailsPage.addToCart();

        List<ShoppingCartProduct> cartProducts = productDetailsPage.getCartProductsFromHeader();

        Assert.assertEquals(1, cartProducts.size());

        ShoppingCartProduct cartProduct = cartProducts.get(0);
        Product actualProduct = cartProduct.getInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .isEqualTo(expectedProduct);
        assertThat(cartProduct.getCount()).isEqualTo(1);

        cartProduct.selectMaxAvailableCount();
        assertThat(productDetailsPage.isAddToCartButtonDisabled())
                .as("'Add to cart' button should be disabled")
                .isTrue();

        productDetailsPage.goToCartPage();
        assertThat(productDetailsPage.waitTillTitleContains(CART_PAGE_TITLE))
                .as("Title should be %s", CART_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_29: add any product to Cart, remove it, verify cart is empty
     */
    @Test(groups = {"cartTestsForGuest"})
    public void removeProductFromCartTest() {
        productDetailsPage.goToCartPage();

        CartPage cartPage = page(CartPage.class);
        List<ShoppingCartProduct> cartProducts = cartPage.getProducts();
        assertThat(cartProducts).hasSize(1);

        ShoppingCartProduct productToDelete = cartProducts.get(0);
        productToDelete.removeFromCart();

        assertThat(cartPage.waitTillCartIsEmpty())
                .as("Cart should be empty")
                .isTrue();
    }

    /**
     * MAF_30: Find element of count more than 1, add to cart, increase its count till 2,
     * continue to payment flow, verify it was purchased successfully
     */
    @Test(groups = {"plpTests"})
    public void updateItemCountOnCart(){
        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        for (int i = 0; i < womenShoesPLPage.getProducts().size(); i++) {
            womenShoesPLPage.getProducts().get(i).goToProductDetailsPage();
            if (!productDetailsPage.getStockIndicatorText().contains("Only 1 left")) {
                break;
            }
            // page is loading for a long time with back()
            open(WOMEN_SHOES_PL_PAGE_URL);
        }
        productDetailsPage.addToCart();
        productDetailsPage.goToCartPage();

        CartPage cartPage = page(CartPage.class);
        ShoppingCartProduct cartProduct = cartPage.getProducts().get(0);

        assertThat(cartProduct.getCount()).isEqualTo(1);

        int requiredProductCount = 2;
        cartProduct.selectCount(requiredProductCount);

        String expectedPrice = getStringValueOfPrice(cartProduct.getInformation().getPrice() * requiredProductCount);
        assertThat(cartPage.waitTillTotalPriceIs(expectedPrice))
                .as("total price should be %s", expectedPrice)
                .isTrue();

        cartPage.checkout();
        productDetailsPage.checkoutAsGuest(SIGN_UP_TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, SIGN_UP_TEST_USER);
        checkoutPage.payWithBankCard(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_27: Add product to cart as a logged-in user, add product to cart as guest user,
     * login, verify both products are in the cart, continue to payment flow, verify it was purchased successfully
     */
    @Test(groups = {"cartTestsForRegisteredUser"})
    public void mergeCartItemsTest(){
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(productDetailsPage.getInformation());

        open(WOMEN_SHOES_PL_PAGE_URL);
        womenShoesPLPage.logout();

        CategoryProduct anotherProduct = womenShoesPLPage.getProducts().get(1);
        anotherProduct.goToProductDetailsPage();
        productDetailsPage.addToCart();
        expectedProducts.add(productDetailsPage.getInformation());

        productDetailsPage.checkoutAsLoggedInUser(LOGIN_TEST_USER);

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        List<Product> actualProducts = checkoutPage
                .getSummaryProducts()
                .stream()
                .map(CheckoutSummaryProduct::getInformation)
                .collect(Collectors.toList());
        assertThat(actualProducts)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedProducts);

        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.payWithBankCard(TEST_BANK_CARD);
        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_22: Add product to cart, go to cart, enter invalid promo code,
     * verify invalid promo code message is displayed
     */
    @Test(groups = {"cartTestsForRegisteredUser", "clearCartTearUp"})
    public void useInvalidPromoCodeTest() {
        productDetailsPage.goToCartPage();

        CartPage cartPage = page(CartPage.class);
        cartPage.applyPromoCode(INVALID_PROMO_CODE);
        assertThat(cartPage.isInvalidPromoCodeTextVisible())
                .as("Invalid promo code text should be visible")
                .isTrue();
    }

    /**
     * MAF_22: Add product to cart, go to cart, enter THATAUTO valid promo code (50 AED discount)
     * verify total price is reduced by 50 AED
     */
    @Test(groups = {"cartTestsForRegisteredUser", "clearCartAndPromoCodeTearUp"})
    public void useValidPromoCodeTest() {
        productDetailsPage.goToCartPage();

        CartPage cartPage = page(CartPage.class);
        int priceWithoutPromoCodeDiscount = Utils.getIntegerValueOfPrice(cartPage.getTotalPrice());

        cartPage.applyPromoCode(VALID_PROMO_CODE);
        assertThat(cartPage.waitTillTotalPriceIs(Utils.getStringValueOfPrice(priceWithoutPromoCodeDiscount - 50)))
                .as("Total price should be less by 50 AED")
                .isTrue();
    }
}
