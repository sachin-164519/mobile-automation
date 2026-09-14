package com.velocitor.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class OrderCompletionPage extends BasePage {

    private static final By CHECKOUT_COMPLETE_SCREEN =
            AppiumBy.accessibilityId("checkout complete screen");

    private static final By HEADER_TEXT =
            By.xpath("//android.widget.TextView[@text='Checkout Complete']");
    private static final By THANK_YOU_TEXT =
            By.xpath("//android.widget.TextView[@text='Thank you for your order']");
    private static final By SHIPMENT_TEXT =
            By.xpath("//android.widget.TextView[@text='Your new swag is on its way']");
    private static final By DISPATCH_TEXT =
            By.xpath("//android.widget.TextView[contains(@text,'Your order has been dispatched')]");
    private static final By CONTINUE_SHOPPING_BUTTON =
            AppiumBy.accessibilityId("Continue Shopping button");

    public OrderCompletionPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(CHECKOUT_COMPLETE_SCREEN, Duration.ofSeconds(10));
    }

    public String getHeaderText() {
        return waitVisible(HEADER_TEXT).getText();
    }

    public String getThankYouText() {
        return waitVisible(THANK_YOU_TEXT).getText();
    }

    public String getShipmentText() {
        return waitVisible(SHIPMENT_TEXT).getText();
    }

    public String getDispatchText() {
        return waitVisible(DISPATCH_TEXT).getText();
    }

    public void clickContinueShopping() {
        waitVisible(CONTINUE_SHOPPING_BUTTON).click();
    }
}