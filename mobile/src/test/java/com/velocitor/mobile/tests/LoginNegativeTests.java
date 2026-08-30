package com.velocitor.mobile.tests;

import com.velocitor.mobile.base.BaseMobileTest;
import com.velocitor.mobile.pages.HomePage;
import com.velocitor.mobile.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginNegativeTests extends BaseMobileTest {

    @Test
    public void loginWithEmptyCredentials_shouldFail() {
        HomePage homePage = new HomePage(driver);

        homePage.openLoginFromMenu();

        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginScreenDisplayed(), "Login screen is not displayed");

        loginPage.login("", "");

        boolean validationVisible =
                loginPage.isUsernameErrorDisplayed()
                        || loginPage.isPasswordErrorDisplayed()
                        || loginPage.isGenericErrorDisplayed();

        Assert.assertTrue(validationVisible, "Validation error should be displayed for empty login");
    }

    @Test
    public void loginWithInvalidCredentials_shouldFail() {
        HomePage homePage = new HomePage(driver);

        homePage.openLoginFromMenu();

        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginScreenDisplayed(), "Login screen is not displayed");

        loginPage.login("invalid@example.com", "wrongpassword");

        boolean validationVisible =
                loginPage.isUsernameErrorDisplayed()
                        || loginPage.isPasswordErrorDisplayed()
                        || loginPage.isGenericErrorDisplayed();

        Assert.assertTrue(validationVisible, "Invalid login should show an error");
    }
}