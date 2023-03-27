package that.composites.pop_ups;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import that.composites.AbstractPageComposite;

public class AccountPopUp extends AbstractPageComposite {
    @FindBy(xpath = "//button[contains(text(), 'Login')]")
    private SelenideElement loginButton;

    @FindBy(xpath = "//button[contains(text(), 'Logout')]")
    private SelenideElement logoutButton;

    @FindBy(xpath = "//button[contains(text(), 'register now')]")
    private SelenideElement registerNowButton;

    @FindBy(className = "user-details")
    private SelenideElement userDetailsButton;

    public void clickUserDetailsButton() {
        userDetailsButton.click();
    }

    public String getUserDetailsButtonText() {
        return userDetailsButton.getText();
    }

    public void clickLoginButton() {
        loginButton.shouldBe(Condition.enabled).click();
    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    public void clickRegisterButton() {
        registerNowButton.click();
    }
}
