package com.github.osvalda.rambutan.uitest.tests.access;

import com.github.osvalda.rambutan.uitest.framework.BaseTest;
import com.github.osvalda.rambutan.uitest.pageobjects.*;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTests extends BaseTest {

    private final String unknownEmailAddress =  "unknown.user@test.domain.com";
    private final String unknownPassword =  "Secret123!";

    SoftAssertions softAssert;

    @BeforeMethod(alwaysRun = true)
    public void testSetup() {
        softAssert = new SoftAssertions();
    }

    @Test(groups = "smoke")
    @Description("<font color=\"#4682b4\">Access Test<ol><li><b>Unsuccessful login</b> attempt due to unknown user credentials</li>" +
            "<li>Successful <b>login</b> attempt</li><li>Welcome page check</li><li>Successful <b>log out</b></li></ol>")
    @Severity(SeverityLevel.BLOCKER)
    public void accessTest() {
        LoginPage login = new LoginPage(getDriver(), getSiteUrl() + loginUrl);
        login.openLoginPage();
        checkIfLoginIsLoadedCorrectly(login.isSignInButtonVisible(), login.isPasswordInputMasked());

        login.loginWithCredentials(unknownEmailAddress, unknownPassword);
        checkIfErrorMessageIsAvailableAndCorrect(login.isResultVisible(), login.getResultMessage());

        AccountPage accountPage = login.loginWithCredentials(getEmail(), getPassword());
        checkIfWelcomeMessageIsAvailable(accountPage.getWelcomeMessage());

        accountPage.clickLogout();
        checkIfLoginIsLoadedCorrectly(login.isSignInButtonVisible(), login.isPasswordInputMasked());

        softAssert.assertAll();
    }

    @Step("Assert that Error notification is appeared and correct")
    private void checkIfErrorMessageIsAvailableAndCorrect(boolean isAvailable, String message) {
        softAssert.assertThat(isAvailable)
                .as("The Notification Bar is not present")
                .isTrue();
        softAssert.assertThat(message)
                .as("The notification message is not correct")
                .isEqualTo("Invalid Email or Password");
    }

    @Step("Assert that welcome message is presented and correct")
    private void checkIfWelcomeMessageIsAvailable(String welcomeMessage) {
        softAssert.assertThat(welcomeMessage)
                .as("Welcome message is wrong!")
                .isEqualTo("Hi, Demo User");
    }

    @Step("Assert that login page is loaded correctly.")
    private void checkIfLoginIsLoadedCorrectly(boolean isSignInButtonVisible, boolean isPasswordMasked) {
        softAssert.assertThat(isSignInButtonVisible)
                .as("Login button should be displayed")
                .isTrue();
        softAssert.assertThat(isPasswordMasked)
                .as("Password input filed should have type 'password'!")
                .isTrue();
        softAssert.assertAll();
    }

}
