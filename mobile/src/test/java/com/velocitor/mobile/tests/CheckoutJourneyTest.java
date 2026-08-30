package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.CartPage;
import com.velocitor.mobile.pages.CheckoutPage;
import com.velocitor.mobile.pages.HomePage;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.OrderCompletionPage;
import com.velocitor.mobile.pages.PaymentPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import com.velocitor.mobile.pages.ProductDetailsPage;
import com.velocitor.mobile.pages.ReviewOrderPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutJourneyTest extends BaseMobileTest {

    @Test
    public void verifyUserCanLoginAddProductCheckoutAndPlaceOrder() {
        HomePage homePage = new HomePage(driver);

        // 1. Home page must open
        Assert.assertTrue(homePage.isProductsScreenDisplayed(), "Home/products screen is not displayed");

        // 2. Open menu and login
        homePage.openLoginFromMenu();

        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginScreenDisplayed(), "Login page is not displayed");

        loginPage.login("bob@example.com", "10203040");

        // After login, app returns to products/catalog screen
        ProductCatalogPage productCatalogPage = new ProductCatalogPage(driver);
        Assert.assertTrue(productCatalogPage.isDisplayed(), "Product catalog is not displayed");

        // 3. Select a product
        String productName = "Sauce Labs Backpack";
        Assert.assertTrue(productCatalogPage.isProductVisible(productName),
                "Product is not visible in catalog: " + productName);

        productCatalogPage.openProduct(productName);

        // 4. Product details page
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        Assert.assertTrue(productDetailsPage.isDisplayed(), "Product details page is not displayed");
        Assert.assertEquals(productDetailsPage.getProductTitle(), productName,
                "Selected product title does not match expected product");

        // 5. Add product to cart
        productDetailsPage.addToCart();

        // 6. Open cart from top-right icon
        homePage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isDisplayed(), "Cart page is not displayed");
        Assert.assertTrue(cartPage.isItemDisplayed(productName),
                "Selected product is not shown in cart");

        // 7. Continue to checkout
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isDisplayed(), "Checkout page is not displayed");

        checkoutPage.enterShippingInfo(
                "Rebecca Winter",
                "Mandorley 112",
                "Entrance 1",
                "Truro",
                "Cornwall",
                "89750",
                "United Kingdom"
        );
        checkoutPage.clickToPayment();

        // 8. Payment page
        PaymentPage paymentPage = new PaymentPage(driver);
        Assert.assertTrue(paymentPage.isDisplayed(), "Payment page is not displayed");

        paymentPage.enterPaymentInfo(
                "Rebecca Winter",
                "3258 1265 7568 789",
                "03/30",
                "123"
        );
        // paymentPage.selectBillingSameAsShipping();
        paymentPage.clickReviewOrder();

        // 9. Review order page
        ReviewOrderPage reviewOrderPage = new ReviewOrderPage(driver);
        Assert.assertTrue(reviewOrderPage.isDisplayed(), "Review order page is not displayed");
        Assert.assertEquals(reviewOrderPage.getProductName(), productName,
                "Review order product does not match selected product");
        Assert.assertEquals(reviewOrderPage.getTotalNumberText(), "1 item");
        Assert.assertEquals(reviewOrderPage.getTotalPriceText(), "$35.98");

        reviewOrderPage.clickPlaceOrder();

        // 10. Order completion page
        OrderCompletionPage orderCompletionPage = new OrderCompletionPage(driver);
        Assert.assertTrue(orderCompletionPage.isDisplayed(), "Order completion screen is not displayed");
        Assert.assertEquals(orderCompletionPage.getHeaderText(), "Checkout Complete");
        Assert.assertEquals(orderCompletionPage.getThankYouText(), "Thank you for your order");
    }
}