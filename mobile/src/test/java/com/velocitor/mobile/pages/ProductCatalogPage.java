package com.velocitor.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class ProductCatalogPage extends BasePage {

    private static final By PRODUCTS_SCREEN = AppiumBy.accessibilityId("products screen");
    private static final By CART_BADGE = AppiumBy.accessibilityId("cart badge");
    private static final By STORE_ITEM = AppiumBy.accessibilityId("store item");
    private static final By STORE_ITEM_TEXT = AppiumBy.accessibilityId("store item text");

    public ProductCatalogPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(PRODUCTS_SCREEN, Duration.ofSeconds(10));
    }

    public boolean isProductVisible(String productName) {
        By locator = By.xpath(
                "//*[@content-desc='store item' and .//*[@content-desc='store item text' and @text='" + productName + "']]"
        );
        return isVisible(locator, Duration.ofSeconds(5));
    }

    public void openProduct(String productName) {
        By locator = By.xpath(
                "//*[@content-desc='store item' and .//*[@content-desc='store item text' and @text='" + productName + "']]"
        );
        waitVisible(locator).click();
    }

    public void openCart() {
        waitVisible(CART_BADGE).click();
    }

    public int getProductCount() {
        return driver.findElements(STORE_ITEM).size();
    }
}
