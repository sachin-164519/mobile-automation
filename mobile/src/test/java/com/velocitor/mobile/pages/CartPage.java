package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.util.List;

public class CartPage extends BasePage {

    private static final By CART_ITEM_TITLES = AppiumBy.accessibilityId("test-Item title");
    private static final By CHECKOUT_BUTTON = AppiumBy.accessibilityId("test-CHECKOUT");

    public CartPage(AndroidDriver driver) {
        super(driver);
    }

    public List<String> getItemTitles() {
        return driver.findElements(CART_ITEM_TITLES)
                .stream()
                .map(element -> element.getText())
                .toList();
    }

    public int getItemCount() {
        return driver.findElements(CART_ITEM_TITLES).size();
    }

    public CheckoutPage proceedToCheckout() {
        waitVisible(CHECKOUT_BUTTON).click();
        return new CheckoutPage(driver);
    }
}
