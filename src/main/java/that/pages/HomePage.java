package that.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {
    public static final Integer EXPECTED_NUMBER_OF_BANNERS = 3;
    @FindBy(className = "sec-promo-banner")
    private ElementsCollection bannerButtons;

    public Boolean areBannerButtonsVisible() {
        bannerButtons.shouldBe(CollectionCondition.size(EXPECTED_NUMBER_OF_BANNERS));
        bannerButtons.forEach(banner -> banner.shouldBe(Condition.visible));
        return true;
    }
}
