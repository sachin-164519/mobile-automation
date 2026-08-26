package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.CartPage;
import com.velocitor.mobile.pages.CheckoutPage;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Checkout")
class CheckoutTests extends BaseMobileTest {

    @Test
    @DisplayName("completing checkout with valid info shows an order confirmation")
    void completingCheckoutShowsConfirmation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        ProductCatalogPage catalogPage = new ProductCatalogPage(driver);
        catalogPage.addFirstProductToCart();
        catalogPage.openCart();

        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        checkoutPage.enterShippingInfo("Ada", "Lovelace", "94107");
        checkoutPage.finishOrder();

        assertThat(checkoutPage.isOrderConfirmed())
                .as("a confirmation screen should be shown after finishing the order")
                .isTrue();
    }
}
