package that.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import that.pages.HomePage;
import that.pages.users_pages.AccountPage;
import that.pages.users_pages.OrdersPage;
import utils.Utils;

import java.io.File;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.Assertions.assertThat;
import static that.entities.User.LOGIN_TEST_USER;
import static that.test_data.Categories.AccountPageSections.*;
import static that.test_data.Categories.AccountPageSections.YOUR_ACCOUNT;

public class AccountPageTests extends AbstractBaseTest {
    private File downloadedFile;
    private AccountPage accountPage;

    @BeforeMethod
    public void accountPageSetUp(){
        homePage = openBrowserOnPage("/", HomePage.class);
        homePage.login(LOGIN_TEST_USER);
        homePage.goToAccountPage();

        accountPage = page(AccountPage.class);
    }

    @AfterMethod(onlyForGroups = "downloadReceiptTest")
    public void downloadReceiptTearDown(){
        downloadedFile.delete();
    }

    /**
     * MAF_14: Login, go to Account page, verify main Account page elements are displayed
     */
    @Test
    public void checkAccountPageDetailsTest() {
        String[] yourAccountSubsections = new String[]{
                UPCOMING_ORDERS.getSection(),
                YOUR_WISHLIST.getSection(),
                ACCOUNT_SIGN_OUT.getSection()};

        for (String section : accountPage.getSectionNames()) {
            accountPage.clickSection(section);

            if (section.equals(YOUR_ACCOUNT.getSection())) {
                accountPage.clickSection(YOUR_ACCOUNT.getSection());
                assertThat(accountPage.waitTillSubsectionsContains(yourAccountSubsections))
                        .as("Your account section header should contain all subsections")
                        .isTrue();
            } else {
                assertThat(accountPage.waitTillSubsectionsContains(section))
                        .as("Section headers should contain %s", section)
                        .isTrue();
            }
        }
    }

    /**
     * MAF_28: Log in, go to orders page, download invoice for order,
     * verify receipt is downloaded and its content
     */
    @Test(groups = {"downloadReceiptTest"}, dependsOnGroups = "cartTestsForRegisteredUser")
    public void downloadReceiptTest(){
        accountPage.clickSection(ORDERS.getSection());
        OrdersPage ordersPage = page(OrdersPage.class);
        downloadedFile = accountPage.downloadInvoiceForOrder(0);
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
