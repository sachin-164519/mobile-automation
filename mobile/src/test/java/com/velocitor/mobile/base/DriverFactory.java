package com.velocitor.mobile.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static AppiumDriver createDriver() throws Exception {

        String platform = System.getProperty("platform", "android");

        String appiumServerUrl = System.getProperty(
                "appium.server.url",
                "http://127.0.0.1:4723"
        );

        if (platform.equalsIgnoreCase("android")) {
            return createAndroidDriver(appiumServerUrl);
        }

        if (platform.equalsIgnoreCase("ios")) {
            return createIOSDriver(appiumServerUrl);
        }

        throw new IllegalArgumentException(
                "Unsupported platform: " + platform
        );
    }

    private static AndroidDriver createAndroidDriver(
            String appiumServerUrl) throws Exception {

        String deviceName = System.getProperty(
                "device.name",
                "Pixel 9 Pro"
        );

        String platformVersion = System.getProperty(
                "platform.version",
                "17"
        );

        String appPath = System.getProperty(
                "app.path",
                Paths.get(
                        "src",
                        "test",
                        "resources",
                        "apps",
                        "MyDemoApp.apk"
                ).toAbsolutePath().toString()
        );

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setApp(appPath)
                .setAppPackage("com.saucelabs.mydemoapp.rn")
                .setAppActivity(
                        "com.saucelabs.mydemoapp.rn.MainActivity"
                )
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutomationName("UiAutomator2");

        return new AndroidDriver(
                new URL(appiumServerUrl),
                options
        );
    }

    private static IOSDriver createIOSDriver(
            String appiumServerUrl) throws Exception {

        String deviceName = System.getProperty(
                "device.name",
                "iPhone 16 Pro"
        );

        String platformVersion = System.getProperty(
                "platform.version",
                "18.0"
        );

        String appPath = System.getProperty(
                "app.path",
                Paths.get(
                        "src",
                        "test",
                        "resources",
                        "apps",
                        "MyDemoApp.app"
                ).toAbsolutePath().toString()
        );

        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setApp(appPath)
                .setBundleId("com.saucelabs.mydemoapp.rn")
                .setAutoAcceptAlerts(true)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAutomationName("XCUITest");

        return new IOSDriver(
                new URL(appiumServerUrl),
                options
        );
    }
}
