package that.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.WishlistProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.users_pages.CartPage;
import that.pages.users_pages.WishlistPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.User.LOGIN_TEST_USER;
import static that.test_data.PageTitlesAndBreadCrumbs.CART_PAGE_TITLE;
import static that.test_data.PageTitlesAndBreadCrumbs.WISHLIST_PAGE_TITLE;

//TODO: Filter out of stock while bug on P2 env
public class WishlistPageTests extends AbstractBaseTest {

    @AfterMethod(onlyForGroups = "clearWishlistAndCartTearUp")
    public void wishlistTearUp() {
        WishlistPage wishlistPage = getWebDriver().getCurrentUrl().endsWith(WISHLIST_PAGE_URL)
                ? page(WishlistPage.class)
                : open(WISHLIST_PAGE_URL, WishlistPage.class);
        womenShoesPLPage.waitTillTitleContains(WISHLIST_PAGE_TITLE);

        if (wishlistPage.getProducts().size() > 0) {
            wishlistPage.removeAllProducts();
        } else {
            CartPage cartPage = open(CART_PAGE_URL, CartPage.class);
            cartPage.waitTillTitleContains(CART_PAGE_TITLE);
            cartPage.removeAllProducts();
        }

        closeWebDriver();
    }

    /**
     * MAF_09: Go to PLP as Guest user, add any product to wishlist,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"plpTests"})
    public void addToWishlistFromPLPForGuestUserTest() {
        CategoryProduct categoryProduct = womenShoesPLPage.getProducts().get(8);
        categoryProduct.addToWishlist();
        Product expectedProduct = categoryProduct.getInformation();

        womenShoesPLPage.goToWishlistPage();
        assertThat(womenShoesPLPage.waitTillTitleContains(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.isRemoveFromWishlistBtnDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink", "sale")
                .isEqualTo(expectedProduct);

        wishlistProduct.moveToBag();
        assertThat(wishlistPage.isProductsCount(0))
                .as("Wishlist page should be empty")
                .isTrue();
    }

    /**
     * MAF_09: Go to PDP as Guest user, add any product to wishlist, go to Wishlist page,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"plpTests"})
    public void addToWishlistFromPDPForGuestUserTest() {
        CategoryProduct categoryProduct = womenShoesPLPage.getProducts().get(8);
        categoryProduct.goToProductDetailsPage();
        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = productDetailsPage.getInformation();

        productDetailsPage.addToWishlist();

        womenShoesPLPage.goToWishlistPage();
        assertThat(womenShoesPLPage.waitTillTitleContains(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.isRemoveFromWishlistBtnDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        wishlistProduct.moveToBag();
        assertThat(wishlistPage.isProductsCount(0))
                .as("Wishlist page should be empty")
                .isTrue();
    }

    /**
     * MAF_10: Go to PLP as Registered user, add any product to wishlist,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"plpTests", "clearWishlistAndCartTearUp"})
    public void addToWishlistFromPLPForRegisteredUserTest() {
        womenShoesPLPage.login(LOGIN_TEST_USER);
        CategoryProduct categoryProduct = womenShoesPLPage.getProducts().get(8);
        categoryProduct.addToWishlist();
        Product expectedProduct = categoryProduct.getInformation();

        womenShoesPLPage.goToWishlistPage();
        assertThat(womenShoesPLPage.waitTillTitleContains(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.isRemoveFromWishlistBtnDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink", "sale")
                .isEqualTo(expectedProduct);

        wishlistProduct.moveToBag();
        assertThat(wishlistPage.isProductsCount(0))
                .as("Wishlist page should be empty")
                .isTrue();
    }

    /**
     * MAF_10: Go to PDP as Registered user, add any product to wishlist, go to Wishlist page,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"plpTests", "clearWishlistAndCartTearUp"})
    public void addToWishlistFromPDPForRegisteredUserTest() {
        womenShoesPLPage.login(LOGIN_TEST_USER);
        CategoryProduct categoryProduct = womenShoesPLPage.getProducts().get(8);
        categoryProduct.goToProductDetailsPage();
        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = productDetailsPage.getInformation();

        productDetailsPage.addToWishlist();

        womenShoesPLPage.goToWishlistPage();
        assertThat(womenShoesPLPage.waitTillTitleContains(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();

        WishlistPage wishlistPage = page(WishlistPage.class);
        List<WishlistProduct> wishlistProducts = wishlistPage.getProducts();
        assertThat(wishlistProducts).hasSize(1);

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        assertThat(wishlistProduct.isRemoveFromWishlistBtnDisplayed())
                .as("Remove from wishlist icon should be displayed")
                .isTrue();

        Product actualProduct = wishlistProduct.getInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        wishlistProduct.moveToBag();
        assertThat(wishlistPage.isProductsCount(0))
                .as("Wishlist page should be empty")
                .isTrue();
    }
}
