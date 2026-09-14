package com.velocitor.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class LoginPage extends BasePage {

    // Confirmed from the XML dump
    private static final By LOGIN_SCREEN = AppiumBy.accessibilityId("login screen");
    private static final By USERNAME_FIELD = AppiumBy.accessibilityId("Username input field");
    private static final By PASSWORD_FIELD = AppiumBy.accessibilityId("Password input field");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("Login button");

    private static final By USERNAME_ERROR = AppiumBy.xpath("//android.widget.TextView[@text=\"Username is required\"]");
    private static final By PASSWORD_ERROR = AppiumBy.xpath("//android.widget.TextView[@text=\"Password is required\"]");
    private static final By GENERIC_ERROR = AppiumBy.xpath("//android.widget.TextView[@text=\"Provided credentials do not match any user in this service.\"]");

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoginScreenDisplayed() {
        return isVisible(LOGIN_SCREEN, Duration.ofSeconds(5));
    }

    public void login(String username, String password) {
        waitVisible(USERNAME_FIELD).clear();
        waitVisible(USERNAME_FIELD).sendKeys(username);

        waitVisible(PASSWORD_FIELD).clear();
        waitVisible(PASSWORD_FIELD).sendKeys(password);

        waitVisible(LOGIN_BUTTON).click();
    }

    public boolean isUsernameErrorDisplayed() {
        return isVisible(USERNAME_ERROR, Duration.ofSeconds(5));
    }

    public boolean isPasswordErrorDisplayed() {
        return isVisible(PASSWORD_ERROR, Duration.ofSeconds(5));
    }

    public boolean isGenericErrorDisplayed() {
        return isVisible(GENERIC_ERROR, Duration.ofSeconds(5));
    }

    public String getUsernameErrorText() {
        return waitVisible(USERNAME_ERROR).getText();
    }

    public String getPasswordErrorText() {
        return waitVisible(PASSWORD_ERROR).getText();
    }

    public String getGenericErrorText() {
        return waitVisible(GENERIC_ERROR).getText();
    }
}
