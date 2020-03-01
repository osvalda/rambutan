package com.github.osvalda.rambutan.uitest.pageobjects;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class LoginPage extends PageObject {

    @FindBy(name = "username")
    private WebElement emailInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()=\"Login\"]")
    private WebElement loginButton;

    @FindBy(className = "resultlogin")
    private WebElement loginResult;

    private String siteUrl;

    public LoginPage(WebDriver driver, String siteUrl) {
        this.driver = driver;
        this.siteUrl = siteUrl;
        PageFactory.initElements(driver, this);
    }

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Opening login page")
    public LoginPage openLoginPage() {
        log.info("Open login page at {}", siteUrl);
        driver.get(siteUrl);
        PageFactory.initElements(driver, this);
        return this;
    }

    @Step("Logging in to Test Site")
    public AccountPage loginWithCredentials(String username, String password) {
        clearWebElement(emailInput);
        emailInput.sendKeys(username);

        clearWebElement(passwordInput);
        passwordInput.sendKeys(password);

        loginButton.click();
        return new AccountPage(driver);
    }

    @Step("Enter credentials")
    public LoginPage enterCredentials(String email, String password) {
        enterEmailAddress(email);
        enterPassword(password);
        return this;
    }

    @Step("Enter email")
    public LoginPage enterEmailAddress(String emailAddress) {
        clearWebElement(emailInput);
        emailInput.sendKeys(emailAddress);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        clearWebElement(passwordInput);
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Click Login button")
    public void clickLoginButton() {
        loginButton.click();
    }

    public boolean isSignInButtonVisible() {
        CONDITION_FACTORY.until(() -> loginButton.isDisplayed());
        return true;
    }

    @Step("Get login result message")
    public String getResultMessage() {
        CONDITION_FACTORY.until(() -> loginResult.isDisplayed());
        return loginResult.getText();
    }
    public boolean isResultVisible() {
        try {
            CONDITION_FACTORY.until(() -> loginResult.isDisplayed());
        } catch (ConditionTimeoutException e) {
            return false;
        }
        return true;
    }

    public boolean isPasswordInputMasked() {
        CONDITION_FACTORY.until(() -> passwordInput.isDisplayed());
        return StringUtils.containsIgnoreCase(passwordInput.getAttribute("type"), "password");
    }

}
