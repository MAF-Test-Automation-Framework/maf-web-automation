package that.tests;

import org.testng.annotations.Test;
import that.composites.products.CategoryProduct;
import that.pages.users_pages.AccountPage;
import that.pages.products_pages.ProductsListPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.User.LOGIN_TEST_USER;
import static that.entities.User.TEST_USER;
import static that.test_data.Categories.AccountPageSection.*;
import static that.test_data.Categories.L1Categories.WOMEN;
import static that.test_data.Categories.L2Categories.SHOES;
import static that.test_data.PageTitlesAndBreadCrumbs.*;

public class HomePageTest extends AbstractBaseTest {
    /**
     * MAF_01: Go to Home page, verify header items are clickable, banners are visible, loaded page is correct
     */
    @Test(groups = {"homePageTests"})
    public void mainElementsExistenceTest() {
        assertThat(homePage.areAllHeaderMenuItemsClickableOrVisible())
                .as("Header menu items should be clickable")
                .isTrue();
        assertThat(homePage.areBannerButtonsVisible())
                .as("Banners should be visible")
                .isTrue();
        assertThat(homePage.doesTitleContain(HOME_PAGE_TITLE))
                .as("Title should be %s", HOME_PAGE_TITLE)
                .isTrue();
    }

    /**
     * MAF_06: Click any l1 category and verify correct page are displayed
     */
    @Test(groups = {"homePageTests"})
    public void l1CategoryTest() {
        homePage.clickHeaderL1Category(String.valueOf(WOMEN));

        assertThat(homePage.doesTitleContain(WOMEN_PLP_TITLE))
                .as("Title should be %s", WOMEN_PLP_TITLE)
                .isTrue();
    }

    /**
     * MAF_07: Click any l2 category and verify correct page and breadcrumb are displayed
     */
    @Test(groups = {"homePageTests"})
    public void l2CategoryTest() {
        homePage.clickHeaderL2Category(String.valueOf(WOMEN), String.valueOf(SHOES));
        String actualWomenShoesPageBreadCrumb = homePage.getBreadCrumbText();

        assertThat(homePage.doesTitleContain(WOMEN_SHOES_PLP_TITLE))
                .as("Title should be %s", WOMEN_SHOES_PLP_TITLE)
                .isTrue();
        assertThat(actualWomenShoesPageBreadCrumb).isEqualTo(WOMEN_SHOES_BREADCRUMB);
    }

    /**
     * MAF_08: Click l3 Sandals category and verify correct page and breadcrumb are displayed
     */
    @Test(groups = {"homePageTests"})
    public void l3CategoryTest() {
        homePage.clickHeaderSandalsL3Category();
        String actualWomenSandalsPageBreadCrumb = homePage.getBreadCrumbText();

        assertThat(homePage.doesTitleContain(WOMEN_SHOES_SANDALS_PLP_TITLE))
                .as("Title should be %s", WOMEN_SHOES_SANDALS_PLP_TITLE)
                .isTrue();
        assertThat(actualWomenSandalsPageBreadCrumb).isEqualTo(WOMEN_SHOES_SANDALS_BREADCRUMB);
    }

    /**
     * MAF_19: Search Dress from header search line, verify found products are dresses
     */
    @Test(groups = {"homePageTests"})
    public void searchProductsTest() {
        String searchedKey = "Dress";

        homePage.searchProductFromHeader(searchedKey);

        ProductsListPage productListPage = page(ProductsListPage.class);
        assertThat(productListPage.doesTitleContain(SEARCH_PAGE_TITLE_PART))
                .as("Does title contain %s", SEARCH_PAGE_TITLE_PART)
                .isTrue();
        List<CategoryProduct> foundProducts = productListPage.getProducts().subList(0, 4);
        foundProducts.get(0).scrollToProduct();
        assertThat(foundProducts)
                .allSatisfy(product -> assertThat(product.getProductInformation().getProductName()).contains(searchedKey));
    }

    /**
     * MAF_14: Login, go to Account page, verify main Account page elements are displayed
     */
    @Test(groups = {"homePageTests"})
    public void accountPageDetailsTest() {
        String[] yourAccountSubsections = new String[]{
                COMPLETE_YOUR_DETAILS.getSection(),
                YOUR_WISHLIST.getSection(),
                ACCOUNT_SIGN_OUT.getSection()};

        homePage.login(LOGIN_TEST_USER);
        homePage.clickUserDetailsHeaderAccountPopUpBtn();
        AccountPage accountPage = page(AccountPage.class);

        for (String section : accountPage.getSectionNames()) {
            accountPage.clickSection(section);

            if (section.equals(YOUR_ACCOUNT.getSection())) {
                accountPage.clickSection(YOUR_ACCOUNT.getSection());
                assertThat(accountPage.isSectionHeadersContains(yourAccountSubsections))
                        .as("Section headers should contain")
                        .isTrue();
            } else {
                assertThat(accountPage.isSectionHeadersContains(section))
                        .as("Section headers should contain %s", section)
                        .isTrue();
            }
        }
    }

    /**
     * MAF_11:
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
}
