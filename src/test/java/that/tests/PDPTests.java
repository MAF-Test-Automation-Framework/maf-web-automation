package that.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.ShoppingCartProduct;
import that.composites.products.WishlistProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.users_pages.CartPage;
import that.pages.users_pages.CheckoutPage;
import that.pages.users_pages.WishlistPage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.BankCard.TEST_BANK_CARD;
import static that.entities.DeliveryAddress.TEST_ADDRESS;
import static that.entities.User.TEST_USER;
import static that.test_data.Categories.FilterOptions.*;
import static that.test_data.Categories.SortOptions.RECOMMENDED;
import static that.test_data.PageTitlesAndBreadCrumbs.*;
import static utils.Utils.getStringValueOfPrice;

public class PDPTests extends AbstractBaseTest {

    /**
     * MAF_02: Click any PLP product and verify correct page and breadcrumb are displayed
     */
    @Test(groups = {"pdpTests"})
    public void pdpPageTest() {
        // TODO: PDP should pageear for related brand?
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        Product expectedProduct = firstProduct.getProductInformation();
        String expectedBreadCrumbEnd = expectedProduct.getProductName();
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        assertThat(firstProductDetailsPage.doesTitleContain(expectedBreadCrumbEnd))
                .as("Does title contain %s", expectedBreadCrumbEnd)
                .isTrue();

        String actualBreadCrumb = firstProductDetailsPage.getBreadCrumbText();
        assertThat(actualBreadCrumb).contains(WOMEN_SHOES_BREADCRUMB);
        assertThat(actualBreadCrumb).contains(expectedBreadCrumbEnd);
    }

    /**
     * MAF_03: Click any PLP product, verify product detail tabs are clickable, product info is visible
     */
    @Test(groups = {"pdpTests"})
    public void productDetailsSectionTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        firstProductDetailsPage.clickProductDetailsLink();

        for (int tabIndex = 0; tabIndex < 3; tabIndex++) {
            firstProductDetailsPage.clickProductDetailsTabByIndex(tabIndex);
            assertThat(firstProductDetailsPage.isPDTabContentVisible(tabIndex))
                    .as("Content should be displayed for tab %d", tabIndex)
                    .isTrue();
        }
    }

    /**
     * MAF_04: Click any PLP product, verify Product ID code from Product Details
     */
    @Test(groups = {"pdpTests"})
    public void axSkuIdTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        firstProductDetailsPage.clickProductDetailsLink();

        String currentUrl = getWebDriver().getCurrentUrl();
        String expectedProductId = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
        assertThat(firstProductDetailsPage.getProductId()).isEqualTo(expectedProductId);
    }

    /**
     * MAF_20: Go to Product Details page, add product to cart,
     * verify Product from Cart pop up windows is the same as PDP Product, max count,
     * click 'Go to bag' button, verify user is redirected to the correct page
     */
    @Test(groups = {"pdpTests"})
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
    @Test(groups = {"pdpTests"})
    public void removeProductFromCartTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        firstProductDetailsPage.clickAddToCartButton();
        firstProductDetailsPage.clickGoToBagHeaderCartPopUpButton();

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
     * MAF_09: Go to PLP as Guest user, add any product to wishlist,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"pdpTests"})
    public void addToWishlistFromPLPForGuestUserTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.addToWishlist();
        Product expectedProduct = firstProduct.getProductInformation();

        womenShoesPLPage.clickHeaderWishlistButton();
        assertThat(womenShoesPLPage.doesTitleContain(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.removeFromWishlistBtnIsDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getProductInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        wishlistProduct.clickMoveToBagButton();
        assertThat(wishlistPage.getProducts()).hasSize(0);
    }

    /**
     * MAF_09: Go to PDP as Guest user, add any product to wishlist, go to Wishlist page,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"pdpTests"})
    public void addToWishlistFromPDPForGuestUserTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();
        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = firstProductDetailsPage.getProductInformation();

        firstProductDetailsPage.clickAddToWishlistButton();

        womenShoesPLPage.clickHeaderWishlistButton();
        assertThat(womenShoesPLPage.doesTitleContain(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.removeFromWishlistBtnIsDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getProductInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        wishlistProduct.clickMoveToBagButton();
        assertThat(wishlistPage.getProducts()).hasSize(0);
    }

    /**
     * MAF_17, MAF_18: Sort list of products, scroll down to see more products, verify it's sorted correctly
     */
    @Test(groups = {"pdpTests"}, dataProvider = "TestDataForSorting")
    public void sortProductListTest(String sortOption, Comparator<Product> comparator) {
        int twoPagesProductsCount = 40;
        womenShoesPLPage.sortProductsBy(sortOption);

        List<CategoryProduct> productElements = womenShoesPLPage.getProductsMoreThanOrEqual(twoPagesProductsCount);
        List<Product> products = womenShoesPLPage.convertProductElementsToEntities(productElements);
        List<Product> sortedProducts = products
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(products).usingRecursiveComparison().isEqualTo(sortedProducts);
    }

    /**
     * MAF_15: Filter products by 2 criteria, verify correct products are displayed, verify products quantity
     */
    @Test(groups = {"pdpTests"})
    public void filterProductListTest() {
        womenShoesPLPage.filterProducts(BRAND.getOption(), JUIN_BRAND.getOption());
        int expectedFilteredProductsCount = womenShoesPLPage
                .filterProducts(CATEGORY.getOption(), ON_SALE_CATEGORY.getOption());
        List<CategoryProduct> allFilteredProducts = womenShoesPLPage.getProductsMoreThanOrEqual(expectedFilteredProductsCount);

        assertThat(allFilteredProducts).hasSize(expectedFilteredProductsCount);
        assertThat(allFilteredProducts)
                .allSatisfy(product -> assertThat(product.getProductInformation().getBrand()).isEqualTo(JUIN_BRAND.getOption()));
        assertThat(allFilteredProducts)
                .allSatisfy(product -> assertThat(product.getProductInformation().getSale()).isTrue());
    }

    /**
     * MAF_16: Verify Recommended option selected by default
     */
    @Test(groups = {"pdpTests"})
    public void defaultSortingOptionTest() {
        assertThat(womenShoesPLPage.getSelectedOption()).isEqualTo(RECOMMENDED.getOption());
    }

    /**
     * MAF_12: Verify shopping user operation as Guest
     */
    @Test(groups = {"pdpTests"})
    public void shopAsGuestTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        firstProductDetailsPage.clickAddToCartButton();
        firstProductDetailsPage.clickCheckoutNowButton();
        firstProductDetailsPage.checkoutAsGuest(TEST_USER.getEmail());

        CheckoutPage checkoutPage = page(CheckoutPage.class);
        checkoutPage.fillFormForGuest(TEST_ADDRESS, TEST_USER);
        checkoutPage.fillPaymentInfo(TEST_BANK_CARD);

        assertThat(checkoutPage.getOrderConfirmationText())
                .contains("Order number:")
                .contains(TEST_USER.getEmail());
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
     * MAF_13: Verify shopping user operation as Registered user
     */
    @Test(groups = {"pdpTests"}, dependsOnMethods = {"signInSignUpTest"})
    public void shopAsRegisteredUserTest() {
        womenShoesPLPage.login(TEST_USER);

        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();

        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        firstProductDetailsPage.clickAddToCartButton();
        firstProductDetailsPage.clickCheckoutNowButton();

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
    @Test(groups = {"pdpTests"})
    public void updateItemCountOnCart(){
        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        for (int i = 0; i < womenShoesPLPage.getProducts().size(); i++) {
            womenShoesPLPage.getProducts().get(i).clickProduct();
            if (!firstProductDetailsPage.getStockIndicatorText().contains("Only 1 left")) {
                break;
            }
            // page is loading for a long time with back()
            open("/c/women-shoes");
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
}

