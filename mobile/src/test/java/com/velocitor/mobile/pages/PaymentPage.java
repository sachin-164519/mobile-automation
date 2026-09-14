package com.velocitor.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class PaymentPage extends BasePage {

    private static final By PAYMENT_SCREEN = AppiumBy.accessibilityId("checkout payment screen");
    private static final By CONTAINER_HEADER = AppiumBy.accessibilityId("container header");

    private static final By FULL_NAME_FIELD = AppiumBy.accessibilityId("Full Name* input field");
    private static final By CARD_NUMBER_FIELD = AppiumBy.accessibilityId("Card Number* input field");
    private static final By EXPIRATION_DATE_FIELD = AppiumBy.accessibilityId("Expiration Date* input field");
    private static final By SECURITY_CODE_FIELD = AppiumBy.accessibilityId("Security Code* input field");

    private static final By FULL_NAME_ERROR = AppiumBy.accessibilityId("Full Name*-error-message");
    private static final By CARD_NUMBER_ERROR = AppiumBy.accessibilityId("Card Number*-error-message");
    private static final By EXPIRATION_DATE_ERROR = AppiumBy.accessibilityId("Expiration Date*-error-message");
    private static final By SECURITY_CODE_ERROR = AppiumBy.accessibilityId("Security Code*-error-message");

    private static final By BILLING_SAME_AS_SHIPPING_CHECKBOX =
            AppiumBy.accessibilityId("checkbox for My billing address is the same as my shipping address.");
    private static final By REVIEW_ORDER_BUTTON = AppiumBy.accessibilityId("Review Order button");

    public PaymentPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(PAYMENT_SCREEN, Duration.ofSeconds(10));
    }

    public String getHeaderText() {
        return waitVisible(CONTAINER_HEADER).getText();
    }

    public void enterPaymentInfo(String fullName, String cardNumber, String expiryDate, String securityCode) {
        waitVisible(FULL_NAME_FIELD).clear();
        waitVisible(FULL_NAME_FIELD).sendKeys(fullName);

        waitVisible(CARD_NUMBER_FIELD).clear();
        waitVisible(CARD_NUMBER_FIELD).sendKeys(cardNumber);

        waitVisible(EXPIRATION_DATE_FIELD).clear();
        waitVisible(EXPIRATION_DATE_FIELD).sendKeys(expiryDate);

        waitVisible(SECURITY_CODE_FIELD).clear();
        waitVisible(SECURITY_CODE_FIELD).sendKeys(securityCode);
    }

    public void selectBillingSameAsShipping() {
        waitVisible(BILLING_SAME_AS_SHIPPING_CHECKBOX).click();
    }

    public boolean isBillingSameAsShippingSelected() {
        return isVisible(BILLING_SAME_AS_SHIPPING_CHECKBOX, Duration.ofSeconds(5));
    }

    public boolean isFullNameErrorDisplayed() {
        return isVisible(FULL_NAME_ERROR, Duration.ofSeconds(5));
    }

    public boolean isCardNumberErrorDisplayed() {
        return isVisible(CARD_NUMBER_ERROR, Duration.ofSeconds(5));
    }

    public boolean isExpirationDateErrorDisplayed() {
        return isVisible(EXPIRATION_DATE_ERROR, Duration.ofSeconds(5));
    }

    public boolean isSecurityCodeErrorDisplayed() {
        return isVisible(SECURITY_CODE_ERROR, Duration.ofSeconds(5));
    }

    public void clickReviewOrder() {
        waitVisible(REVIEW_ORDER_BUTTON).click();
    }
}