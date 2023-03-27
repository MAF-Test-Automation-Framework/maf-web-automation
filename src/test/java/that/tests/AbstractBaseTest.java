package that.tests;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import that.composites.products.CategoryProduct;
import that.entities.Product;
import that.pages.AbstractPage;
import that.pages.HomePage;
import that.pages.products_pages.ProductDetailsPage;
import that.pages.products_pages.ProductsListPage;
import that.pages.users_pages.CartPage;

import java.util.Comparator;

import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static that.entities.User.LOGIN_TEST_USER;
import static that.test_data.Categories.SortOptions.HIGHEST_PRICE;
import static that.test_data.Categories.SortOptions.LOWEST_PRICE;

public class AbstractBaseTest {
    public final static String WOMEN_SHOES_PL_PAGE_URL = "/c/women-shoes";
    public final static String CART_PAGE_URL = "/cart";
    public final static String WISHLIST_PAGE_URL = "/wishlist";
    protected HomePage homePage;
    protected ProductsListPage womenShoesPLPage;
    protected ProductDetailsPage productDetailsPage;

    @BeforeSuite
    public void driverSetUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        Configuration.browser = "chrome";
        Configuration.browserCapabilities = chromeOptions;
        //        Configuration.baseUrl = "https://www.thatconceptstore.com/en-ae";
        Configuration.baseUrl = "https://that.c1xjddw2-majidalfu1-p2-public.model-t.cc.commerce.ondemand.com/en-ae";
//        Configuration.baseUrl = "https://that.c1xjddw2-majidalfu1-s2-public.model-t.cc.commerce.ondemand.com/en-ae/";
        Configuration.headless = false;
        Configuration.timeout = 40000;
        Configuration.fileDownload = FOLDER;
    }

    @BeforeMethod(onlyForGroups = {"plpTests"})
    public void plpSetUp() {
        womenShoesPLPage = openBrowserOnPage(WOMEN_SHOES_PL_PAGE_URL, ProductsListPage.class);
    }

    @BeforeMethod(onlyForGroups = {"cartTestsForGuest"})
    public void cartSetUpForGuest() {
        womenShoesPLPage = openBrowserOnPage(WOMEN_SHOES_PL_PAGE_URL, ProductsListPage.class);
        addProductFromPlpToCart(0);
    }

    @BeforeMethod(onlyForGroups = {"cartTestsForRegisteredUser"})
    public void cartSetUpForRegisteredUser() {
        womenShoesPLPage = openBrowserOnPage(WOMEN_SHOES_PL_PAGE_URL, ProductsListPage.class);
        womenShoesPLPage.login(LOGIN_TEST_USER);
        addProductFromPlpToCart(7);
    }

    @AfterMethod
    public void tearUp() {
        closeWebDriver();
    }

    @AfterMethod(onlyForGroups = "clearCartTearUp")
    public void cartTearUp(){
        CartPage cartPage = open(CART_PAGE_URL, CartPage.class);
        cartPage.removeAllProducts();
        closeWebDriver();
    }

    @DataProvider(name = "TestDataForSorting")
    public Object[][] getSortingData() {
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        return new Object[][]
                {
                        {LOWEST_PRICE.getOption(), comparator},
                        {HIGHEST_PRICE.getOption(), comparator.reversed()}
                };
    }

    public void addProductFromPlpToCart(int indexOfProduct){
        CategoryProduct product = womenShoesPLPage.getProducts().get(indexOfProduct);
        product.goToProductDetailsPage();

        productDetailsPage = page(ProductDetailsPage.class);
        productDetailsPage.addToCart();
    }

    protected <T extends AbstractPage> T openBrowserOnPage(String url, Class<T> pageObjectClass) {
        T page = open(url, pageObjectClass);
        getWebDriver().manage().window().maximize();
        page.closeCookieNotification();
        return page;
    }
}
