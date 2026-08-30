package com.velocitor.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
