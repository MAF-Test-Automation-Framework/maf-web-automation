package that.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.CheckoutSummaryProduct;
import that.composites.products.ShoppingCartProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.products_pages.ProductsListPage;
import that.pages.users_pages.CartPage;
import that.pages.users_pages.CheckoutPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.BankCard.TEST_BANK_CARD;
import static that.entities.DeliveryAddress.TEST_ADDRESS;
import static that.entities.User.LOGIN_TEST_USER;
import static that.entities.User.TEST_USER;
import static that.test_data.PageTitlesAndBreadCrumbs.CART_PAGE_TITLE;
import static that.test_data.PageTitlesAndBreadCrumbs.WOMEN_SHOES_PLP_TITLE;
import static utils.Utils.getStringValueOfPrice;

public class CartTest extends AbstractBaseTest{

    @BeforeMethod(onlyForGroups = {"cartTestsForGuest"})
    public void cartSetUpForGuest() {
        womenShoesPLPage = openPage(WOMEN_SHOES_PL_URL, ProductsListPage.class);
        addProductFromPlpToCart(0);
    }

    @BeforeMethod(onlyForGroups = {"cartTestsForRegisteredUser"})
    public void cartSetUpForRegisteredUser() {
        womenShoesPLPage = openPage(WOMEN_SHOES_PL_URL, ProductsListPage.class);
        womenShoesPLPage.login(LOGIN_TEST_USER);
        addProductFromPlpToCart(0);
    }

    public void addProductFromPlpToCart(int indexOfProduct){
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(indexOfProduct);
        firstProduct.clickProduct();

        productDetailsPage = page(ProductDetailsPage.class);
        productDetailsPage.clickAddToCartButton();
    }

    /**
     * MAF_11: Sign in, verify signed-in username, login, verify logged in username
     */
    @Test(groups = {"homePageTests"})
    public void signInSignUpTest() {
        homePage.signUp(TEST_USER);
        assertThat(homePage.getHeaderAccountPopUpUserDetailsText())
                .contains(TEST_USER.getFirstName())
                .contains(TEST_USER.getLastName());

        homePage.logout();

        homePage.login(TEST_USER);
        assertThat(homePage.getHeaderAccountPopUpUserDetailsText())
                .contains(TEST_USER.getFirstName())
                .contains(TEST_USER.getLastName());
    }

    /**
     * MAF_20: Go to Product Details page, add product to cart,
     * verify Product from Cart pop up windows is the same as PDP Product, max count,
     * click 'Go to bag' button, verify user is redirected to the correct page
     */
    @Test(groups = {"plpTests"})
    public void addToBagPopUpTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = firstProductDetailsPage.getProductInformation();
        firstProductDetailsPage.clickAddToCartButton();

        List<ShoppingCartProduct> cartProducts = firstProductDetailsPage.getCartPopUpProductsFromHeader();

        Assert.assertEquals(1, cartProducts.size());

        ShoppingCartProduct cartProduct = cartProducts.get(0);
        Product actualProduct = cartProduct.getProductInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .isEqualTo(expectedProduct);
        assertThat(cartProduct.getProductCount()).isEqualTo(1);

        cartProduct.selectMaxAvailableProductCount();
        assertThat(firstProductDetailsPage.isAddToCartButtonDisabled())
                .as("'Add to cart' button should be disabled")
                .isTrue();

        firstProductDetailsPage.clickGoToBagHeaderCartPopUpButton();
        assertThat(firstProductDetailsPage.doesTitleContain(CART_PAGE_TITLE))
                .as("Title should be %s", CART_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_29: add any product to Cart, remove it, verify cart is empty
     */
    @Test(groups = {"cartTestsForGuest"})
    public void removeProductFromCartTest() {
        productDetailsPage.clickGoToBagHeaderCartPopUpButton();

        CartPage cartPage = page(CartPage.class);
        List<ShoppingCartProduct> cartProducts = cartPage.getProducts();
        assertThat(cartProducts).hasSize(1);

        ShoppingCartProduct productToDelete = cartProducts.get(0);
        productToDelete.removeFromCart();

        assertThat(cartPage.isCartEmpty())
                .as("Cart should be empty")
                .isTrue();
    }

    /**
     * MAF_12: Verify shopping user operation as Guest
     */
    @Test(groups = {"cartTestsForGuest"})
    public void shopAsGuestTest() {
        productDetailsPage.clickCheckoutNowButton();
        productDetailsPage.checkoutAsGuest(TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, TEST_USER);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(TEST_USER.getEmail());
    }

    /**
     * MAF_13: Verify shopping user operation as Registered user
     */
    @Test(groups = {"cartTestsForRegisteredUser"}, dependsOnMethods = {"signInSignUpTest"})
    public void shopAsRegisteredUserTest() {
        productDetailsPage.clickCheckoutNowButton();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(TEST_USER.getEmail());
    }

    /**
     * MAF_30: Find element of count more than 1, add to cart, increase its count till 2,
     * continue to payment flow, verify it was purchased successfully
     */
    @Test(groups = {"plpTests"})
    public void updateItemCountOnCart(){
        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        for (int i = 0; i < womenShoesPLPage.getProducts().size(); i++) {
            womenShoesPLPage.getProducts().get(i).clickProduct();
            if (!firstProductDetailsPage.getStockIndicatorText().contains("Only 1 left")) {
                break;
            }
            // page is loading for a long time with back()
            open(WOMEN_SHOES_PL_URL);
        }
        firstProductDetailsPage.clickAddToCartButton();
        firstProductDetailsPage.clickGoToBagHeaderCartPopUpButton();

        CartPage cartPage = page(CartPage.class);
        ShoppingCartProduct cartProduct = cartPage.getProducts().get(0);

        assertThat(cartProduct.getProductCount()).isEqualTo(1);

        int requiredProductCount = 2;
        cartProduct.selectProductCount(requiredProductCount);

        String expectedPrice = getStringValueOfPrice(cartProduct.getProductInformation().getPrice() * requiredProductCount);
        assertThat(cartPage.isTotalPrice(expectedPrice))
                .as("total price should be %s", expectedPrice)
                .isTrue();

        cartPage.clickCheckoutButton();
        firstProductDetailsPage.checkoutAsGuest(TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, TEST_USER);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(TEST_USER.getEmail());
    }

    /**
     * MAF_27: Add product to cart as a logged-in user, add product to cart as guest user,
     * login, verify both products are in the cart, continue to payment flow, verify it was purchased successfully
     */
    @Test(groups = {"cartTestsForRegisteredUser"})
    public void mergeCartItemsTest(){
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(productDetailsPage.getProductInformation());

        open(WOMEN_SHOES_PL_URL);
        womenShoesPLPage.logout();

        CategoryProduct secondProduct = womenShoesPLPage.getProducts().get(1);
        secondProduct.clickProduct();
        productDetailsPage.clickAddToCartButton();
        expectedProducts.add(productDetailsPage.getProductInformation());

        productDetailsPage.clickCheckoutNowButton();
        productDetailsPage.checkoutAndLogin(LOGIN_TEST_USER);

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        List<Product> actualProducts = checkoutPage
                .getProducts()
                .stream()
                .map(CheckoutSummaryProduct::getProductInformation)
                .collect(Collectors.toList());
        assertThat(actualProducts)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedProducts);

        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);
        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(TEST_USER.getEmail());
    }
}
