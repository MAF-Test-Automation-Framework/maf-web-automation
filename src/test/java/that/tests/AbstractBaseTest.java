package that.tests;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import that.entities.Product;
import that.pages.HomePage;
import that.pages.ProductsListPage;

import java.util.Comparator;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static that.test_data.Categories.SortOptions.HIGHEST_PRICE;
import static that.test_data.Categories.SortOptions.LOWEST_PRICE;

public class AbstractBaseTest {
    protected HomePage homePage;
    protected ProductsListPage womenShoesPLPage;

    @BeforeMethod(onlyForGroups = {"homePageTests"})
    public void homePageSetUp() {
        driverSetUp();
    }

    @BeforeMethod(onlyForGroups = {"pdpTests"})
    public void pdpSetUp() {
        driverSetUp();
        womenShoesPLPage = open("/c/women-shoes", ProductsListPage.class);
    }

    @DataProvider(name = "TestDataForSorting")
    public Object[][] getData() {
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        return new Object[][]
                {
                        {LOWEST_PRICE.getOption(), comparator},
                        {HIGHEST_PRICE.getOption(), comparator.reversed()}
                };
    }

    private void driverSetUp() {
        Configuration.browser = "chrome";
//        Configuration.baseUrl = "https://www.thatconceptstore.com/en-ae";
        Configuration.baseUrl = "https://that.c1xjddw2-majidalfu1-p2-public.model-t.cc.commerce.ondemand.com/en-ae";
        Configuration.headless = false;
        Configuration.timeout = 40000;

        homePage = open("/", HomePage.class);
        getWebDriver().manage().window().maximize();

        homePage.clickCookieNotificationCloseButton();
    }

}
