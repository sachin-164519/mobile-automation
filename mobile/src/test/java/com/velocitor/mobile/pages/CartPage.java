package com.velocitor.mobile.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class CartPage extends BasePage {

    private static final By CART_SCREEN = AppiumBy.accessibilityId("cart screen");
    private static final By PRODUCT_ROW = AppiumBy.accessibilityId("product row");
    private static final By PRODUCT_LABEL = AppiumBy.accessibilityId("product label");
    private static final By TOTAL_NUMBER = AppiumBy.accessibilityId("total number");
    private static final By TOTAL_PRICE = AppiumBy.accessibilityId("total price");
    private static final By PROCEED_TO_CHECKOUT_BUTTON = AppiumBy.accessibilityId("Proceed To Checkout button");
    private static final By REMOVE_ITEM = AppiumBy.accessibilityId("remove item");

    public CartPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(CART_SCREEN, Duration.ofSeconds(10));
    }

    public List<String> getItemTitles() {
        return driver.findElements(PRODUCT_ROW).stream()
                .map(row -> row.findElement(PRODUCT_LABEL).getText())
                .toList();
    }

    public int getItemCount() {
        return driver.findElements(PRODUCT_ROW).size();
    }

    public String getTotalItemsText() {
        return waitVisible(TOTAL_NUMBER).getText();
    }

    public String getTotalPriceText() {
        return waitVisible(TOTAL_PRICE).getText();
    }

    public boolean isItemDisplayed(String productName) {
        return driver.findElements(PRODUCT_ROW).stream()
                .anyMatch(row -> row.findElement(PRODUCT_LABEL).getText().equals(productName));
    }

    public void removeItem(String productName) {
        WebElement row = driver.findElements(PRODUCT_ROW).stream()
                .filter(r -> r.findElement(PRODUCT_LABEL).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart: " + productName));

        row.findElement(REMOVE_ITEM).click();
    }

    public CheckoutPage proceedToCheckout() {
        waitVisible(PROCEED_TO_CHECKOUT_BUTTON).click();
        return new CheckoutPage(driver);
    }
}
