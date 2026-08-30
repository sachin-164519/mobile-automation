package com.velocitor.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static AndroidDriver createDriver() throws Exception {
        String appiumServerUrl = System.getProperty("appium.server.url", "http://127.0.0.1:4723");
        String deviceName = System.getProperty("device.name", "Pixel 9 Pro");
        String platformVersion = System.getProperty("platform.version", "17");

        String appPath = System.getProperty(
                "app.path",
                Paths.get("src", "test", "resources", "apps", "MyDemoApp.apk").toAbsolutePath().toString()
        );

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setApp(appPath)
                .setAppPackage("com.saucelabs.mydemoapp.rn")
                .setAppActivity("com.saucelabs.mydemoapp.rn.MainActivity")
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutomationName("UiAutomator2");

        return new AndroidDriver(new URL(appiumServerUrl), options);
    }
}
