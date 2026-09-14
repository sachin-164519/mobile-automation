package com.velocitor.mobile.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;

public class BasePage {
    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    public BasePage(AppiumDriver driver2) {
        this.driver = driver2;
        this.wait = new WebDriverWait(driver2, Duration.ofSeconds(15));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isVisible(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void dismissAndroidSystemDialogIfVisible() {
        By dontShowAgain = By.id("android:id/button1");
        By okButton = By.id("android:id/button2");

        if (driver.findElements(dontShowAgain).size() > 0) {
            driver.findElement(dontShowAgain).click();
        } else if (driver.findElements(okButton).size() > 0) {
            driver.findElement(okButton).click();
        }
    }
}
