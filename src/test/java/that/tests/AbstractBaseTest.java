package that.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import that.pages.HomePage;
import that.pages.ProductsListPage;

public class AbstractBaseTest {
    public final static String HOME_PAGE_URL = "https://that.c1xjddw2-majidalfu1-p2-public.model-t.cc.commerce.ondemand.com/en-ae";
    //    public final static String HOME_PAGE_URL = "https://www.thatconceptstore.com/en-ae";
    public final static String PRODUCT_LIST_PAGE_URL = "https://that.c1xjddw2-majidalfu1-p2-public.model-t.cc.commerce.ondemand.com/en-ae/c/women-shoes";
    //    public final static String PRODUCT_LIST_PAGE_URL = "https://www.thatconceptstore.com/en-ae/c/women-shoes";

    protected WebDriver driver;
    protected HomePage homePage;
    protected ProductsListPage womenShoesPLPage;

    @BeforeMethod(onlyForGroups = {"homePageTests"})
    public void homePageSetUp() {
        driverSetUp();
        driver.get(HOME_PAGE_URL);

        // TODO: May be it won't be needed
        homePage = new HomePage(driver);
        homePage.clickNotificationTestCloseButton();
        homePage.clickCookieNotificationCloseButton();
    }

    @BeforeMethod(onlyForGroups = {"pdpTests"})
    public void pdpSetUp() {
        driverSetUp();
        driver.get(PRODUCT_LIST_PAGE_URL);

        womenShoesPLPage = new ProductsListPage(driver);
        womenShoesPLPage.clickNotificationTestCloseButton();
        womenShoesPLPage.clickCookieNotificationCloseButton();
    }

    private void driverSetUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
//        options.addArguments("-- headless");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
