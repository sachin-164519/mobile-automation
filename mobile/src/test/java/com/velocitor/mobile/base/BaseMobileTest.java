package com.velocitor.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseMobileTest {
    protected AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        driver = DriverFactory.createDriver();
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
}