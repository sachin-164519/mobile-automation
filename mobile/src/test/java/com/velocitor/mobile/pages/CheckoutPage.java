package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class CheckoutPage extends BasePage {

    private static final By CHECKOUT_ADDRESS_SCREEN = AppiumBy.accessibilityId("checkout address screen");
    private static final By CONTAINER_HEADER = AppiumBy.accessibilityId("container header");

    private static final By FULL_NAME_FIELD = AppiumBy.accessibilityId("Full Name* input field");
    private static final By ADDRESS_LINE_1_FIELD = AppiumBy.accessibilityId("Address Line 1* input field");
    private static final By ADDRESS_LINE_2_FIELD = AppiumBy.accessibilityId("Address Line 2 input field");
    private static final By CITY_FIELD = AppiumBy.accessibilityId("City* input field");
    private static final By STATE_REGION_FIELD = AppiumBy.accessibilityId("State/Region input field");
    private static final By ZIP_CODE_FIELD = AppiumBy.accessibilityId("Zip Code* input field");
    private static final By COUNTRY_FIELD = AppiumBy.accessibilityId("Country* input field");

    private static final By TO_PAYMENT_BUTTON = AppiumBy.accessibilityId("To Payment button");

    public CheckoutPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(CHECKOUT_ADDRESS_SCREEN, Duration.ofSeconds(10));
    }

    public String getHeaderText() {
        return waitVisible(CONTAINER_HEADER).getText();
    }

    public void enterShippingInfo(String fullName, String addressLine1, String addressLine2,
                                 String city, String stateRegion, String zipCode, String country) {
        waitVisible(FULL_NAME_FIELD).clear();
        waitVisible(FULL_NAME_FIELD).sendKeys(fullName);

        waitVisible(ADDRESS_LINE_1_FIELD).clear();
        waitVisible(ADDRESS_LINE_1_FIELD).sendKeys(addressLine1);

        waitVisible(ADDRESS_LINE_2_FIELD).clear();
        waitVisible(ADDRESS_LINE_2_FIELD).sendKeys(addressLine2);

        waitVisible(CITY_FIELD).clear();
        waitVisible(CITY_FIELD).sendKeys(city);

        waitVisible(STATE_REGION_FIELD).clear();
        waitVisible(STATE_REGION_FIELD).sendKeys(stateRegion);

        waitVisible(ZIP_CODE_FIELD).clear();
        waitVisible(ZIP_CODE_FIELD).sendKeys(zipCode);

        waitVisible(COUNTRY_FIELD).clear();
        waitVisible(COUNTRY_FIELD).sendKeys(country);
    }

    public void clickToPayment() {
        waitVisible(TO_PAYMENT_BUTTON).click();
    }
}
