package com.velocitor.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final By OPEN_MENU_BUTTON = AppiumBy.accessibilityId("open menu");
    private static final By LONGPRESS_RESET_APP = AppiumBy.accessibilityId("longpress reset app");
    private static final By SORT_BUTTON = AppiumBy.accessibilityId("sort button");
    private static final By CART_BADGE = AppiumBy.accessibilityId("cart badge");
    private static final By PRODUCTS_SCREEN = AppiumBy.accessibilityId("products screen");
    private static final By PRODUCTS_HEADER = AppiumBy.accessibilityId("container header");

    private static final By MENU_ITEM_LOGIN = AppiumBy.accessibilityId("menu item log in");
    private static final By MENU_ITEM_LOGOUT = AppiumBy.accessibilityId("menu item log out");

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isProductsScreenDisplayed() {
        return isVisible(PRODUCTS_SCREEN, Duration.ofSeconds(10));
    }

    public boolean isProductsHeaderDisplayed() {
        return isVisible(PRODUCTS_HEADER, Duration.ofSeconds(10));
    }

    public String getProductsHeaderText() {
        return waitVisible(By.xpath("//android.widget.TextView[@text='Products']")).getText();
    }

    public void openMenu() {
        waitVisible(OPEN_MENU_BUTTON).click();
    }

    public void tapSortButton() {
        waitVisible(SORT_BUTTON).click();
    }

    public void openCart() {
        waitVisible(CART_BADGE).click();
    }

    public void resetApp() {
        waitVisible(LONGPRESS_RESET_APP).click();
    }

    public boolean isProductVisible(String productName) {
        return isVisible(productTextLocator(productName), Duration.ofSeconds(10));
    }

    public void clickProduct(String productName) {
        waitVisible(productTextLocator(productName)).click();
    }

    public void openLoginFromMenu() {
        openMenu();
        waitVisible(MENU_ITEM_LOGIN).click();
    }

    public void openLogoutFromMenu() {
        openMenu();
        waitVisible(MENU_ITEM_LOGOUT).click();
    }

    public boolean isMenuLoginVisible() {
        openMenu();
        return isVisible(MENU_ITEM_LOGIN, Duration.ofSeconds(5));
    }

    public boolean isMenuLogoutVisible() {
        openMenu();
        return isVisible(MENU_ITEM_LOGOUT, Duration.ofSeconds(5));
    }

    private By productTextLocator(String productName) {
        return By.xpath("//*[@content-desc='store item text' and @text='" + productName + "']");
    }
}