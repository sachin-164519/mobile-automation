package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Product catalog / listing screen, shown immediately after a successful login.
 */
public class ProductCatalogPage extends BasePage {

    private static final By SCREEN_TITLE = AppiumBy.accessibilityId("test-PRODUCTS");
    private static final By ADD_TO_CART_BUTTONS = AppiumBy.accessibilityId("test-ADD TO CART");
    private static final By CART_ICON = AppiumBy.accessibilityId("test-Cart");
    private static final By CART_BADGE = AppiumBy.accessibilityId("test-Cart badge");

    public ProductCatalogPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(SCREEN_TITLE, java.time.Duration.ofSeconds(10));
    }

    /**
     * Adds the first product in catalog order to the cart.
     *
     * <p>The "ADD TO CART" accessibility id repeats once per product card, so
     * {@code findElements(...).get(0)} lands on whichever product is listed
     * first (Sauce Labs Backpack, as of this app's default catalog order).
     * Targeting a specific product <em>by name</em> would mean walking up
     * from a name match to its sibling button - straightforward with the real
     * element tree in front of you in Appium Inspector, left as a next step
     * here since it can't be verified without a running session.
     */
    public void addFirstProductToCart() {
        List<WebElement> addButtons = driver.findElements(ADD_TO_CART_BUTTONS);
        addButtons.get(0).click();
    }

    public void openCart() {
        waitVisible(CART_ICON).click();
    }

    public String getCartBadgeCount() {
        return waitVisible(CART_BADGE).getText();
    }
}
