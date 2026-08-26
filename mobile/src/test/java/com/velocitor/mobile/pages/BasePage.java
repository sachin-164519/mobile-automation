package com.velocitor.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Shared plumbing for page objects: an explicit wait, never a hardcoded
 * {@code Thread.sleep()}. Emulators are slow and variable, and hardcoded
 * sleeps are one of the most common sources of mobile-suite flakiness -
 * they're either too short (flaky) or too long (slow suite) and usually end
 * up being both over time as the app changes.
 */
public abstract class BasePage {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isVisible(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
