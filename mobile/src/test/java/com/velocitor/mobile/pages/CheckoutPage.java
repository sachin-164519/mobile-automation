package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Covers the full checkout flow: shipping info -> overview -> confirmation.
 * Modeled as one page object rather than three because the screens share a
 * single linear flow with no branching a test would need to target
 * independently - splitting it further would add indirection without adding
 * clarity. If checkout grows more steps or branches (promo codes, saved
 * addresses), that's the point to split it out.
 */
public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME_FIELD = AppiumBy.accessibilityId("test-First Name");
    private static final By LAST_NAME_FIELD = AppiumBy.accessibilityId("test-Last Name");
    private static final By POSTAL_CODE_FIELD = AppiumBy.accessibilityId("test-Zip/Postal Code");
    private static final By CONTINUE_BUTTON = AppiumBy.accessibilityId("test-CONTINUE");
    private static final By FINISH_BUTTON = AppiumBy.accessibilityId("test-FINISH");
    private static final By CONFIRMATION_HEADER = AppiumBy.accessibilityId("test-CHECKOUT COMPLETE!");

    public CheckoutPage(AndroidDriver driver) {
        super(driver);
    }

    public void enterShippingInfo(String firstName, String lastName, String postalCode) {
        waitVisible(FIRST_NAME_FIELD).sendKeys(firstName);
        driver.findElement(LAST_NAME_FIELD).sendKeys(lastName);
        driver.findElement(POSTAL_CODE_FIELD).sendKeys(postalCode);
        driver.findElement(CONTINUE_BUTTON).click();
    }

    public void finishOrder() {
        waitVisible(FINISH_BUTTON).click();
    }

    public boolean isOrderConfirmed() {
        return isVisible(CONFIRMATION_HEADER, java.time.Duration.ofSeconds(10));
    }
}
