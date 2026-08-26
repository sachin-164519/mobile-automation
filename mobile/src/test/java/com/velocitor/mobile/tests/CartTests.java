package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.CartPage;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cart")
class CartTests extends BaseMobileTest {

    private ProductCatalogPage catalogPage;

    @BeforeEach
    void logIn() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        catalogPage = new ProductCatalogPage(driver);
    }

    @Test
    @DisplayName("adding a product updates the cart badge count")
    void addingItemToCartUpdatesCartBadge() {
        catalogPage.addFirstProductToCart();

        assertThat(catalogPage.getCartBadgeCount())
                .as("cart badge should reflect one item added")
                .isEqualTo("1");
    }

    @Test
    @DisplayName("an added product appears in the cart")
    void cartScreenListsAddedItem() {
        catalogPage.addFirstProductToCart();
        catalogPage.openCart();

        CartPage cartPage = new CartPage(driver);

        assertThat(cartPage.getItemCount())
                .as("cart should contain exactly the one item added")
                .isEqualTo(1);
    }
}
