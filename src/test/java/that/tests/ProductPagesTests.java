/**
 * Tests for Product list and Product Details pages
 */
package that.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.entities.Product;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.products_pages.ProductsListPage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static that.test_data.Categories.FilterOptions.*;
import static that.test_data.Categories.SortOptions.RECOMMENDED;
import static that.test_data.PageTitlesAndBreadCrumbs.WOMEN_SHOES_BREADCRUMB;

public class ProductPagesTests extends AbstractBaseTest {
    @BeforeMethod(onlyForGroups = {"pdpTests"})
    public void pdpSetUp() {
        womenShoesPLPage = openBrowserOnPage(WOMEN_SHOES_PL_PAGE_URL, ProductsListPage.class);

        CategoryProduct product = womenShoesPLPage.getProducts().get(0);
        product.goToProductDetailsPage();

        productDetailsPage = page(ProductDetailsPage.class);
        productDetailsPage.goToProductDetails();
    }

    /**
     * MAF_02: Click any PLP product and verify correct page and breadcrumb are displayed
     */
    @Test(groups = {"plpTests"})
    public void checkPdpPageTest() {
        CategoryProduct product = womenShoesPLPage.getProducts().get(0);
        Product expectedProduct = product.getInformation();
        String expectedBreadCrumbEnd = expectedProduct.getProductName();
        product.goToProductDetailsPage();

        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        assertThat(productDetailsPage.waitTillTitleContains(expectedBreadCrumbEnd))
                .as("Does title contain %s", expectedBreadCrumbEnd)
                .isTrue();

        String actualBreadCrumb = productDetailsPage.getBreadCrumbText();
        assertThat(actualBreadCrumb).contains(WOMEN_SHOES_BREADCRUMB);
        assertThat(actualBreadCrumb).contains(expectedBreadCrumbEnd);
    }

    /**
     * MAF_03: Click any PLP product, verify product detail tabs are clickable, product info is visible
     */
    @Test(groups = {"pdpTests"})
    public void checkProductDetailsSectionTest() {
        for (int tabIndex = 0; tabIndex < 3; tabIndex++) {
            productDetailsPage.clickProductDetailsTabByIndex(tabIndex);
            assertThat(productDetailsPage.isPDTabContentVisible(tabIndex))
                    .as("Content should be displayed for tab %d", tabIndex)
                    .isTrue();
        }
    }

    /**
     * MAF_04: Click any PLP product, verify Product ID code from Product Details
     */
    @Test(groups = {"pdpTests"})
    public void checkAxSkuIdTest() {
        String currentUrl = getWebDriver().getCurrentUrl();
        String expectedProductId = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
        assertThat(productDetailsPage.getProductId()).isEqualTo(expectedProductId);
    }

    /**
     * MAF_17, MAF_18: Sort list of products, scroll down to see more products, verify it's sorted correctly
     */
    @Test(groups = {"plpTests"}, dataProvider = "TestDataForSorting")
    public void sortProductListTest(String sortOption, Comparator<Product> comparator) {
        int twoPagesProductsCount = 40;
        womenShoesPLPage.sortProductsBy(sortOption);

        List<CategoryProduct> productElements = womenShoesPLPage.getMoreProducts(twoPagesProductsCount);
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
    @Test(groups = {"plpTests"})
    public void filterProductListTest() {
        womenShoesPLPage.filterProducts(BRAND.getOption(), JUIN_BRAND.getOption());
        int expectedFilteredProductsCount = womenShoesPLPage
                .filterProducts(CATEGORY.getOption(), ON_SALE_CATEGORY.getOption());
        List<CategoryProduct> allFilteredProducts = womenShoesPLPage.getMoreProducts(expectedFilteredProductsCount);

        assertThat(allFilteredProducts).hasSize(expectedFilteredProductsCount);
        assertThat(allFilteredProducts)
                .allSatisfy(product -> assertThat(product.getInformation().getBrand()).isEqualTo(JUIN_BRAND.getOption()));
        assertThat(allFilteredProducts)
                .allSatisfy(product -> assertThat(product.getInformation().getSale()).isTrue());
    }

    /**
     * MAF_16: Verify Recommended option selected by default
     */
    @Test(groups = {"plpTests"})
    public void checkDefaultSortingOptionTest() {
        assertThat(womenShoesPLPage.getSelectedOption()).isEqualTo(RECOMMENDED.getOption());
    }

    /**
     * MAF_26: Click product that can be in different colors, change its color,
     * verify imageLink is changed, other product definition is the same
     */
    @Test(groups = {"plpTests"})
    public void switchColorTest(){
        CategoryProduct differentColorsProduct = womenShoesPLPage
                .getProducts()
                .stream()
                .filter(product -> product.getColorsNumber()>1)
                .findFirst()
                .get();
        differentColorsProduct.goToProductDetailsPage();
        ProductDetailsPage productDetailsPage = page(ProductDetailsPage.class);
        Product oneColorProduct = productDetailsPage.getInformation();
        productDetailsPage.selectColor(1);
        Product anotherColorProduct = productDetailsPage.getInformation();
        assertThat(oneColorProduct).usingRecursiveComparison()
                .ignoringFields("imageLink")
                .isEqualTo(anotherColorProduct);
        productDetailsPage.isImageLinkChanged(oneColorProduct.getImageLink());
    }
}

