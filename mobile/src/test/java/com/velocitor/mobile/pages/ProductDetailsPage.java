package com.velocitor.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class ProductDetailsPage extends BasePage {

    private static final By PRODUCT_SCREEN = AppiumBy.accessibilityId("product screen");
    private static final By PRODUCT_HEADER = AppiumBy.accessibilityId("container header");
    private static final By PRODUCT_PRICE = AppiumBy.accessibilityId("product price");
    private static final By PRODUCT_DESCRIPTION = AppiumBy.accessibilityId("product description");

    private static final By BLACK_CIRCLE = AppiumBy.accessibilityId("black circle");
    private static final By BLUE_CIRCLE = AppiumBy.accessibilityId("blue circle");
    private static final By GRAY_CIRCLE = AppiumBy.accessibilityId("gray circle");
    private static final By RED_CIRCLE = AppiumBy.accessibilityId("red circle");

    private static final By COUNTER_MINUS = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"counter minus button\"]/android.widget.ImageView");
    private static final By COUNTER_PLUS = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"counter plus button\"]/android.widget.ImageView");
    private static final By COUNTER_AMOUNT = AppiumBy.accessibilityId("counter amount");
    private static final By COUNTER_AMOUNT_TEXT =
        By.xpath("//*[@content-desc='counter amount']//android.widget.TextView");
    private static final By ADD_TO_CART_BUTTON = AppiumBy.accessibilityId("Add To Cart button");

    public ProductDetailsPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(PRODUCT_SCREEN, Duration.ofSeconds(10));
    }

    public String getProductTitle() {
        return waitVisible(PRODUCT_HEADER)
                .findElement(By.className("android.widget.TextView"))
                .getText();
    }

    public String getProductPrice() {
        return waitVisible(PRODUCT_PRICE).getText();
    }

    public String getProductDescription() {
        return waitVisible(PRODUCT_DESCRIPTION).getText();
    }

    public void selectColor(String color) {
        By colorLocator;

        switch (color.toLowerCase()) {
            case "black":
                colorLocator = BLACK_CIRCLE;
                break;
            case "blue":
                colorLocator = BLUE_CIRCLE;
                break;
            case "gray":
                colorLocator = GRAY_CIRCLE;
                break;
            case "red":
                colorLocator = RED_CIRCLE;
                break;
            default:
                throw new IllegalArgumentException("Unsupported color: " + color);
        }

        waitVisible(colorLocator).click();
    }

    public int getQuantity() {
        WebElement counter = waitVisible(COUNTER_AMOUNT);
        String value = counter.findElement(COUNTER_AMOUNT_TEXT).getText().trim();
        return Integer.parseInt(value);
    }

    public void incrementQuantity() {
        waitVisible(COUNTER_PLUS).click();
    }

    public void decrementQuantity() {
        waitVisible(COUNTER_MINUS).click();
    }

    public void addToCart() {
        waitVisible(ADD_TO_CART_BUTTON).click();
    }
}