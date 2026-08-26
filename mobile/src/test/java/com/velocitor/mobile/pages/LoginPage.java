package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Login screen.
 *
 * <p>Locators follow My Demo App's documented {@code test-}-prefixed
 * accessibility-id convention (confirmed against the app family's official
 * sample tests). Treat the exact strings as a starting point, not gospel:
 * verify them with Appium Inspector against the specific APK build in use
 * before relying on this in a real run, and update here if they've drifted.
 */
public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = AppiumBy.accessibilityId("test-Username");
    private static final By PASSWORD_FIELD = AppiumBy.accessibilityId("test-Password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("test-LOGIN");
    private static final By ERROR_MESSAGE = AppiumBy.accessibilityId("test-Error message");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        waitVisible(USERNAME_FIELD).sendKeys(username);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    public boolean isErrorMessageDisplayed() {
        return isVisible(ERROR_MESSAGE, java.time.Duration.ofSeconds(5));
    }

    public String getErrorMessageText() {
        return waitVisible(ERROR_MESSAGE).getText();
    }
}
