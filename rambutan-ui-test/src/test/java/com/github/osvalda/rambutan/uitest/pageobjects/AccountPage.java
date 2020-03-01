package com.github.osvalda.rambutan.uitest.pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AccountPage extends PageObject {

    @FindBy(xpath = "//h3[@class=\"text-align-left\"]")
    private WebElement welcomeMessage;

    @FindBy(xpath = "//a[text()=\"Logout\"]")
    private WebElement logoutButton;

    @FindBy(className = "dropdown-login")
    private WebElement dropDown;

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getWelcomeMessage() {
        CONDITION_FACTORY.until(() -> welcomeMessage.isDisplayed());
        return welcomeMessage.getText();
    }

    @Step("Click on logout")
    public LoginPage clickLogout() {
        CONDITION_FACTORY.until(() -> dropDown.isDisplayed());
        dropDown.click();
        CONDITION_FACTORY.until(() -> logoutButton.isDisplayed());
        logoutButton.click();
        return new LoginPage(driver);
    }

}
