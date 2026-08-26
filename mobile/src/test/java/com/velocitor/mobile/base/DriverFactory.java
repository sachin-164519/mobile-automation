package com.velocitor.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Builds the {@link AndroidDriver} used by mobile tests.
 *
 * <p>Every value is overridable via {@code -D} system property so the same
 * test code runs against a local emulator today and a device-cloud session
 * (Sauce Labs / BrowserStack) later - see README / DESIGN.md for how that
 * swap works in CI, where only the capabilities (not the tests) change.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static AndroidDriver createDriver() throws MalformedURLException {
        String appiumServerUrl = System.getProperty("appium.server.url", "http://127.0.0.1:4723");
        String deviceName = System.getProperty("device.name", "Pixel_6_API_34");
        String platformVersion = System.getProperty("platform.version", "14");
        // Point this at the downloaded My Demo App APK - see README for the releases link.
        String appPath = System.getProperty("app.path", "apps/Android-MyDemoApp.apk");

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setApp(appPath)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        return new AndroidDriver(new URL(appiumServerUrl), options);
    }
}
