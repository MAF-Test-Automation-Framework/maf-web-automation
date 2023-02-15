package that.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import that.composites.CategoryProduct;
import that.composites.ShoppingCartProduct;
import that.composites.WishlistProduct;
import that.entities.Product;
import that.pages.CartPage;
import that.pages.ProductDetailsPage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static that.test_data.PageTitlesAndBreadCrumbs.*;

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
     * verify Product from Wishlist pop up windows is the same as chosen PLP Product
     */
    @Test(groups = {"pdpTests"})
    public void addToWishlistFromPLPForGuestUserTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.addToWishlist();
        Product expectedProduct = firstProduct.getProductInformation();

        womenShoesPLPage.hoverHeaderWishlistButton();
        List<WishlistProduct> wishlistProducts = womenShoesPLPage.getWishlistPopUpProductsFromHeader();
        Assert.assertEquals(1, wishlistProducts.size());

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        Product actualProduct = wishlistProduct.getProductInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        womenShoesPLPage.clickGoToWishlistHeaderPopUpButton();
        assertThat(womenShoesPLPage.doesTitleContain(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_09: Go to PDP as Guest user, add any product to wishlist,
     * verify Product from Wishlist pop up windows is the same as chosen PLP Product
     */
    @Test(groups = {"pdpTests"})
    public void addToWishlistFromPDPForGuestUserTest() {
        CategoryProduct firstProduct = womenShoesPLPage.getProducts().get(0);
        firstProduct.clickProduct();
        ProductDetailsPage firstProductDetailsPage = page(ProductDetailsPage.class);
        Product expectedProduct = firstProductDetailsPage.getProductInformation();

        firstProductDetailsPage.clickAddToWishlistButton();

        womenShoesPLPage.hoverHeaderWishlistButton();
        List<WishlistProduct> wishlistProducts = womenShoesPLPage.getWishlistPopUpProductsFromHeader();
        Assert.assertEquals(1, wishlistProducts.size());

        WishlistProduct wishlistProduct = wishlistProducts.get(0);
        Product actualProduct = wishlistProduct.getProductInformation();
        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(expectedProduct);

        womenShoesPLPage.clickGoToWishlistHeaderPopUpButton();
        assertThat(womenShoesPLPage.doesTitleContain(WISHLIST_PAGE_TITLE))
                .as("Title should be %s", WISHLIST_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_17, MAF_18: Sort list of products, verify it's sorted correctly, refresh page, verify again
     */
    @Test(groups = {"pdpTests"}, dataProvider = "TestDataForSorting")
    public void sortProductListTest(String sortOption, Comparator<Product> comparator) {
        womenShoesPLPage.sortProductsBy(sortOption);

        List<Product> products = womenShoesPLPage.convertProductElementsToEntities(womenShoesPLPage.getProducts());
        List<Product> sortedProducts = products
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(products).usingRecursiveComparison().isEqualTo(sortedProducts);

        womenShoesPLPage.refreshPage();
        List<Product> refreshedProducts = womenShoesPLPage.convertProductElementsToEntities(womenShoesPLPage.getProducts());

        assertThat(refreshedProducts).usingRecursiveComparison().isEqualTo(sortedProducts);
    }

}

