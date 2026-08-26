package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.LoginPage;
import com.velocitor.mobile.pages.ProductCatalogPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Login")
class LoginTests extends BaseMobileTest {

    @Test
    @DisplayName("valid credentials land on the product catalog")
    void validLoginNavigatesToProductCatalog() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        ProductCatalogPage catalogPage = new ProductCatalogPage(driver);

        assertThat(catalogPage.isDisplayed())
                .as("product catalog should be shown after a valid login")
                .isTrue();
    }

    @Test
    @DisplayName("invalid credentials show an error and keep the user on the login screen")
    void invalidLoginShowsErrorMessage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "not-the-real-password");

        assertThat(loginPage.isErrorMessageDisplayed())
                .as("an error message should be shown for invalid credentials")
                .isTrue();
    }
}
