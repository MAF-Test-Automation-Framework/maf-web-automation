package that.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.CheckoutSummaryProduct;
import that.composites.products.ShoppingCartProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.products_pages.ProductsListPage;
import that.pages.users_pages.AccountPage;
import that.pages.users_pages.CartPage;
import that.pages.users_pages.CheckoutPage;
import that.pages.users_pages.OrdersPage;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.BankCard.TEST_BANK_CARD;
import static that.entities.DeliveryAddress.TEST_ADDRESS;
import static that.entities.User.LOGIN_TEST_USER;
import static that.entities.User.SIGN_UP_TEST_USER;
import static that.test_data.Categories.AccountPageSection.ORDERS;
import static that.test_data.PageTitlesAndBreadCrumbs.CART_PAGE_TITLE;
import static utils.Utils.getStringValueOfPrice;

public class CartTest extends AbstractBaseTest{

    File downloadedFile;

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

    @AfterMethod(onlyForGroups = "downloadReceiptTest")
    public void downloadReceiptTearDown(){
        downloadedFile.delete();
    }

    public void addProductFromPlpToCart(int indexOfProduct){
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(indexOfProduct);
        firstProduct.clickProduct();

        productDetailsPage = page(ProductDetailsPage.class);
        productDetailsPage.clickAddToCartButton();
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
        productDetailsPage.checkoutAsGuest(SIGN_UP_TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, SIGN_UP_TEST_USER);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_13: Verify shopping user operation as Registered user
     */
    @Test(groups = {"cartTestsForRegisteredUser"})
    public void shopAsRegisteredUserTest() {
        productDetailsPage.clickCheckoutNowButton();

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForRegisteredUser(TEST_ADDRESS);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(LOGIN_TEST_USER.getEmail());
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
        firstProductDetailsPage.checkoutAsGuest(SIGN_UP_TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, SIGN_UP_TEST_USER);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

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
                .contains(SIGN_UP_TEST_USER.getEmail());
    }

    /**
     * MAF_28: Log in, go to orders page, download invoice for order,
     * verify receipt is downloaded and its content
     */
    @Test(groups = {"plpTests", "downloadReceiptTest"}, dependsOnMethods = "shopAsRegisteredUserTest")
    public void downloadReceiptTest(){
        womenShoesPLPage.login(LOGIN_TEST_USER);
        womenShoesPLPage.clickUserDetailsHeaderAccountPopUpBtn();
        AccountPage accountPage = page(AccountPage.class);
        accountPage.clickSection(ORDERS.getSection());
        OrdersPage ordersPage = page(OrdersPage.class);
        File downloadedFile = accountPage.downloadInvoiceForOrder(0);
        assertThat(downloadedFile.getName()).isEqualTo("receipt.pdf");

        String content = Utils.readPdfFile(downloadedFile);
        assertThat(content).contains(String.format("Invoice Number %s", ordersPage.getOrderNumberText()));
        assertThat(content).contains(String.format("Name %s %s", LOGIN_TEST_USER.getFirstName(), LOGIN_TEST_USER.getLastName()));
        assertThat(content).contains(String.format("Email %s", LOGIN_TEST_USER.getEmail()));
        assertThat(content).contains(String.format("Phone No. %s%s", LOGIN_TEST_USER.getPhoneCountryCode(), LOGIN_TEST_USER.getPhoneNumber()));
        ordersPage.getProductNamesText().forEach(productName -> assertThat(content).containsIgnoringNewLines(productName));
        assertThat(content).contains(String.format("Total (Incl. VAT) %s", ordersPage.getTotalPriceText()));
    }
}
