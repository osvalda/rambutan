package com.github.osvalda.rambutan.uitest.pageobjects;

import com.github.osvalda.rambutan.uitest.framework.BaseTest;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public abstract class PageObject {

    protected WebDriver driver;

    protected static final ConditionFactory CONDITION_FACTORY =
            Awaitility.await().ignoreExceptions().atMost(BaseTest.getTimeout(), TimeUnit.MILLISECONDS);

    protected void clearWebElement(WebElement toBeCleared) {
        toBeCleared.sendKeys(Keys.CONTROL + "a");
        toBeCleared.sendKeys(Keys.DELETE);
    }

    protected boolean isWebelementVisible(WebElement element) {
        try {
            CONDITION_FACTORY.until(() -> element.isDisplayed());
        } catch (ConditionTimeoutException e) {
            return false;
        }
        return true;
    }

}
