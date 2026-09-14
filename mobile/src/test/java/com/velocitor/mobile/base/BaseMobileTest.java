package com.velocitor.mobile.base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseMobileTest {

    protected AppiumDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {

        // Create Android or iOS driver based on -Dplatform
        driver = DriverFactory.createDriver();

        // Handle Android compatibility dialog only
        if (isAndroid()) {
            dismissCompatibilityDialogIfVisible();
        }
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected AppiumDriver getDriver() {
        return driver;
    }

    /**
     * Check whether current execution is Android.
     */
    private boolean isAndroid() {
        String platform = System.getProperty("platform", "android");
        return platform.equalsIgnoreCase("android");
    }

    /**
     * Android-specific compatibility dialog handling.
     */
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
            // Dialog not present or already dismissed
        }
    }
}