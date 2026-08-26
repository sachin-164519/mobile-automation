package com.velocitor.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Fresh app session per test.
 *
 * <p>Relaunching the app for every {@code @Test} (rather than sharing one
 * driver/session across a whole class) costs time, but it's what keeps
 * mobile tests independent: a test that adds an item to the cart shouldn't
 * leave state behind for the next test to trip over. Requirement #1 from the
 * API suite - independence - applies here too, and it's easier to guarantee
 * with a clean app install per test than to carefully reset app state by hand.
 */
public abstract class BaseMobileTest {

    protected AndroidDriver driver;

    @BeforeEach
    void launchApp() throws Exception {
        driver = DriverFactory.createDriver();
    }

    @AfterEach
    void closeApp() {
        if (driver != null) {
            driver.quit();
        }
    }
}
