package that.composites;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class SignInSignUpUserForm extends AbstractPageComposite {
    @FindBy(id = "email")
    private SelenideElement emailInput;

    @FindBy(id = "password")
    private SelenideElement passwordInput;

    @FindBy(id = "first name")
    private SelenideElement firstNameInput;

    @FindBy(id = "last name")
    private SelenideElement lastNameInput;

    @FindBy(id = "phone number")
    private SelenideElement phoneNumberInput;

    @FindBy(css = "[label='Date of Birth']")
    private SelenideElement dateOfBirthSelect;

    @FindBy(className = "year-btn")
    private ElementsCollection dateOfBirthYearOptions;

    @FindBy(className = "month-btn")
    private ElementsCollection dateOfBirthMonthOptions;

    @FindBy(xpath = "//*[@class='vdpCell selectable']")
    private ElementsCollection dateOfBirthDayOptions;

    @FindBy(css = ".vdpComponent .right")
    private SelenideElement changeYearOptionsButton;

    @FindBy(css = "[label='Nationality']")
    private SelenideElement nationalitySelect;

    @FindBy(className = "nationality-bar")
    private ElementsCollection nationalityOptions;

    @FindBy(css = "[for*='titles']")
    private ElementsCollection titleRadiobuttons;

    @FindBy(xpath = "//*[@id='countryCode']/following-sibling::*[3]")
    private SelenideElement countryCodeDropdown;

    @FindBy(xpath = "//*[contains(@class, 'nationality-bar')]/../*[2]")
    private ElementsCollection countryCodeOptions;

    @FindBy(xpath = "//button[contains(text(), 'Sign in')]")
    private SelenideElement signInButton;

    @FindBy(id = "submit-button")
    private SelenideElement signUpButton;

    @FindBy(xpath = "//button[contains(text(), 'Ok')]")
    private SelenideElement emailCheckConfirmationButton;

    public void fillEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void fillPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void fillName(String firstName, String lastName) {
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
    }

    public void fillPhoneNumber(String phoneNumber) {
        phoneNumberInput.sendKeys(phoneNumber);
    }

    public void fillNationality(String nationality) {
        nationalitySelect.click();
        getElementByText(nationalityOptions, nationality).click();
    }

    public void fillDateOfBirth(String year, String month, String day) {
        dateOfBirthSelect.click();
        while (!getAllItemsText(dateOfBirthYearOptions).contains(year)) {
            changeYearOptionsButton.click();
        }
        getElementByText(dateOfBirthYearOptions, year).click();
        getElementByText(dateOfBirthMonthOptions, month).click();
        getElementByText(dateOfBirthDayOptions, day).click();
    }

    public void selectTitle(String title) {
        getElementByText(titleRadiobuttons, title).click();
    }

    public void clickSignInButton() {
        signInButton.click();
    }

    public void clickSignUpButton() {
        signUpButton.click();
    }

    public void clickEmailCheckConfirmationBtn(){
        emailCheckConfirmationButton.click();
    }

    // country code or country name can be passed here
    public void selectCountryCode(String countryCode){
        countryCodeDropdown.click();
        getElementByText(countryCodeOptions, countryCode).click();
    }
}
