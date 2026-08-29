package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.CartPage;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
class CartTests extends BaseMobileTest {

    private ProductCatalogPage catalogPage;

    @BeforeTest
    void logIn() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        catalogPage = new ProductCatalogPage(driver);
    }

    @Test(description = "adding a product updates the cart badge count")
    void addingItemToCartUpdatesCartBadge() {
        catalogPage.addFirstProductToCart();

        assertThat(catalogPage.getCartBadgeCount())
                .as("cart badge should reflect one item added")
                .isEqualTo("1");
    }

    @Test(description = "an added product appears in the cart")
    void cartScreenListsAddedItem() {
        catalogPage.addFirstProductToCart();
        catalogPage.openCart();

        CartPage cartPage = new CartPage(driver);

        assertThat(cartPage.getItemCount())
                .as("cart should contain exactly the one item added")
                .isEqualTo(1);
    }
}
