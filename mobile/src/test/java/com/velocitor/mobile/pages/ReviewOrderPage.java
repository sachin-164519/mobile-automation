package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class ReviewOrderPage extends BasePage {

    private static final By REVIEW_ORDER_SCREEN = AppiumBy.accessibilityId("checkout review order screen");
    private static final By CONTAINER_HEADER = AppiumBy.accessibilityId("container header");

    private static final By PRODUCT_ROW = AppiumBy.accessibilityId("product row");
    private static final By PRODUCT_LABEL = AppiumBy.accessibilityId("product label");
    private static final By PRODUCT_PRICE = AppiumBy.accessibilityId("product price");
    private static final By REVIEW_STAR_1 = AppiumBy.accessibilityId("review star 1");
    private static final By REVIEW_STAR_2 = AppiumBy.accessibilityId("review star 2");
    private static final By REVIEW_STAR_3 = AppiumBy.accessibilityId("review star 3");
    private static final By REVIEW_STAR_4 = AppiumBy.accessibilityId("review star 4");
    private static final By REVIEW_STAR_5 = AppiumBy.accessibilityId("review star 5");
    private static final By BLACK_CIRCLE = AppiumBy.accessibilityId("black circle");

    private static final By DELIVERY_ADDRESS = AppiumBy.accessibilityId("checkout delivery address");
    private static final By PAYMENT_INFO = AppiumBy.accessibilityId("checkout payment info");

    private static final By CHECKOUT_FOOTER = AppiumBy.accessibilityId("checkout footer");
    private static final By TOTAL_NUMBER = AppiumBy.accessibilityId("total number");
    private static final By TOTAL_PRICE = AppiumBy.accessibilityId("total price");

    private static final By PLACE_ORDER_BUTTON = AppiumBy.accessibilityId("Place Order button");

    public ReviewOrderPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(REVIEW_ORDER_SCREEN, Duration.ofSeconds(10));
    }

    public String getHeaderText() {
        return waitVisible(CONTAINER_HEADER).getText();
    }

    public String getProductName() {
        return waitVisible(PRODUCT_LABEL).getText();
    }

    public String getProductPrice() {
        return waitVisible(PRODUCT_PRICE).getText();
    }

    public List<String> getReviewStars() {
        return List.of(
                REVIEW_STAR_1.toString(),
                REVIEW_STAR_2.toString(),
                REVIEW_STAR_3.toString(),
                REVIEW_STAR_4.toString(),
                REVIEW_STAR_5.toString()
        );
    }

    public String getSelectedColor() {
        WebElement blackCircle = waitVisible(BLACK_CIRCLE);
        return blackCircle.getAttribute("content-desc");
    }

    public String getDeliveryAddressText() {
        WebElement addressContainer = waitVisible(DELIVERY_ADDRESS);
        return addressContainer.getText();
    }

    public String getPaymentInfoText() {
        WebElement paymentContainer = waitVisible(PAYMENT_INFO);
        return paymentContainer.getText();
    }

    public String getTotalItemsText() {
        WebElement footer = waitVisible(CHECKOUT_FOOTER);
        return footer.getText();
    }

    public String getTotalNumberText() {
        return waitVisible(TOTAL_NUMBER).getText();
    }

    public String getTotalPriceText() {
        return waitVisible(TOTAL_PRICE).getText();
    }

    public void clickPlaceOrder() {
        waitVisible(PLACE_ORDER_BUTTON).click();
    }
}