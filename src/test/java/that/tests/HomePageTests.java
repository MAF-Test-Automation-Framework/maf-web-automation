package that.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.pages.HomePage;
import that.pages.products_pages.ProductsListPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.User.LOGIN_TEST_USER;
import static that.entities.User.SIGN_UP_TEST_USER;
import static that.test_data.Categories.HeaderMenuCategories.*;
import static that.test_data.PageTitlesAndBreadCrumbs.*;

public class HomePageTests extends AbstractBaseTest {
    @BeforeMethod
    public void homePageSetUp() {
        homePage = openBrowserOnPage("/", HomePage.class);
    }

    /**
     * MAF_01: Go to Home page, verify header items are clickable, banners are visible, loaded page is correct
     */
    @Test
    public void checkHomePageTest() {
        assertThat(homePage.areAllHeaderMenuItemsClickableOrVisible())
                .as("Header menu items should be clickable")
                .isTrue();
        assertThat(homePage.areBannerButtonsVisible())
                .as("Banners should be visible")
                .isTrue();
        assertThat(homePage.waitTillTitleContains(HOME_PAGE_TITLE))
                .as("Title should be %s", HOME_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_06: Click any l1 category and verify correct page are displayed
     */
    @Test
    public void checkL1CategoryTest() {
        homePage.clickHeaderL1Category(WOMEN.getCategory());

        assertThat(homePage.waitTillTitleContains(WOMEN_PLP_TITLE))
                .as("Title should be %s", WOMEN_PLP_TITLE)
                .isTrue();
    }

    /**
     * MAF_07: Click any l2 category and verify correct page and breadcrumb are displayed
     */
    @Test
    public void checkL2CategoryTest() {
        homePage.clickHeaderL2Category(WOMEN.getCategory(), SHOES.getCategory());
        String actualWomenShoesPageBreadCrumb = homePage.getBreadCrumbText();

        assertThat(homePage.waitTillTitleContains(WOMEN_SHOES_PLP_TITLE))
                .as("Title should be %s", WOMEN_SHOES_PLP_TITLE)
                .isTrue();
        assertThat(actualWomenShoesPageBreadCrumb).isEqualTo(WOMEN_SHOES_BREADCRUMB);
    }

    /**
     * MAF_08: Click l3 Sandals category and verify correct page and breadcrumb are displayed
     */
    @Test
    public void checkL3CategoryTest() {
        homePage.clickHeaderL3Category(WOMEN.getCategory(), SHOES.getCategory(), SANDALS.getCategory());
        String actualWomenSandalsPageBreadCrumb = homePage.getBreadCrumbText();

        assertThat(homePage.waitTillTitleContains(WOMEN_SHOES_SANDALS_PLP_TITLE))
                .as("Title should be %s", WOMEN_SHOES_SANDALS_PLP_TITLE)
                .isTrue();
        assertThat(actualWomenSandalsPageBreadCrumb).isEqualTo(WOMEN_SHOES_SANDALS_BREADCRUMB);
    }

    /**
     * MAF_19: Search Dress from header search line, verify found products are dresses
     */
    @Test
    public void searchProductsTest() {
        String searchedKey = "Dress";

        homePage.searchProduct(searchedKey);

        ProductsListPage productListPage = page(ProductsListPage.class);
        assertThat(productListPage.waitTillTitleContains(SEARCH_PAGE_TITLE_PART))
                .as("Does title contain %s", SEARCH_PAGE_TITLE_PART)
                .isTrue();
        List<CategoryProduct> foundProducts = productListPage.getProducts().subList(0, 10);
        foundProducts.get(0).scrollTo();
        assertThat(foundProducts)
                .allSatisfy(product -> assertThat(product.getInformation().getProductName()).contains(searchedKey));
    }

    /**
     * MAF_11: Sign in, verify signed-in username, login, verify logged in username
     */
    @Test
    public void signInSignUpTest() {
        homePage.signUp(SIGN_UP_TEST_USER);
        assertThat(homePage.getUserWelcomeText())
                .contains(SIGN_UP_TEST_USER.getFirstName())
                .contains(SIGN_UP_TEST_USER.getLastName());

        homePage.logout();

        homePage.login(LOGIN_TEST_USER);
        assertThat(homePage.getUserWelcomeText())
                .contains(SIGN_UP_TEST_USER.getFirstName())
                .contains(SIGN_UP_TEST_USER.getLastName());
    }
}
