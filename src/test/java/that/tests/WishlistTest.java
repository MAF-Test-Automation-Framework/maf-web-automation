package that.tests;

import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.composites.products.WishlistProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.users_pages.WishlistPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.test_data.PageTitlesAndBreadCrumbs.WISHLIST_PAGE_TITLE;

public class WishlistTest extends AbstractBaseTest {

    /**
     * MAF_09: Go to PLP as Guest user, add any product to wishlist,
     * verify Product from Wishlist is the same as chosen PLP Product, remove from wishlist icon is displayed,
     * move product to bag, verify it's removed from Wishlist
     */
    @Test(groups = {"plpTests"})
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
    @Test(groups = {"plpTests"})
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
}
