package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.CartPage;
import com.velocitor.mobile.pages.HomePage;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import com.velocitor.mobile.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartAndProductDetailNegativeTests extends BaseMobileTest {

    @Test
    public void cartShouldBeEmptyBeforeAddingAnyProduct() {
        HomePage homePage = new HomePage(driver);

        homePage.openLoginFromMenu();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("bob@example.com", "10203040");

        homePage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isDisplayed(), "Cart page is not displayed");
        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty before adding products");
    }

    @Test
    public void quantityShouldNotGoBelowOne() {
        HomePage homePage = new HomePage(driver);

        homePage.openLoginFromMenu();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("bob@example.com", "10203040");

        ProductCatalogPage productCatalogPage = new ProductCatalogPage(driver);
        productCatalogPage.openProduct("Sauce Labs Backpack");

        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        Assert.assertTrue(productDetailsPage.isDisplayed(), "Product details page is not displayed");

        int initialQty = productDetailsPage.getQuantity();
        productDetailsPage.incrementQuantity();
        productDetailsPage.decrementQuantity();

        Assert.assertTrue(productDetailsPage.getQuantity() >= 1,
                "Quantity should not go below 1");
        Assert.assertTrue(productDetailsPage.getQuantity() <= initialQty,
                "Quantity should not increase after decrement");
    }
}