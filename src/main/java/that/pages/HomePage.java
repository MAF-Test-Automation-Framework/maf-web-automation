package that.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends AbstractPage {

    public static final Integer EXPECTED_NUMBER_OF_BANNERS = 3;
    @FindBy(className = "sec-promo-banner")
    private List<WebElement> bannerButtons;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public Boolean areBannerButtonsVisible(){
        boolean areBannersDisplayed = bannerButtons
                .stream()
                .allMatch(WebElement::isDisplayed);
        return bannerButtons.size() == EXPECTED_NUMBER_OF_BANNERS && areBannersDisplayed;
    }
}
