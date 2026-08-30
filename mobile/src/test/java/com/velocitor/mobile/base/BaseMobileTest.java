package com.velocitor.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseMobileTest {
    protected AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = DriverFactory.createDriver();
        dismissCompatibilityDialogIfVisible();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected AndroidDriver getDriver() {
        return driver;
    }

    private void dismissCompatibilityDialogIfVisible() {
        try {
            By dontShowAgain = By.id("android:id/button1");
            By okButton = By.id("android:id/button2");

            if (driver.findElements(dontShowAgain).size() > 0) {
                driver.findElement(dontShowAgain).click();
                return;
            }

            if (driver.findElements(okButton).size() > 0) {
                driver.findElement(okButton).click();
            }
        } catch (Exception ignored) {
            // dialog not present or already dismissed
        }
    }
}