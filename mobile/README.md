# Mobile Automation Suite

This project contains the Android mobile automation suite for the Sauce Labs My Demo App. It is built with Java, Maven, Appium, and TestNG.

## Overview

The purpose of this project is to automate end-to-end user flows for the My Demo App using Appium and the Page Object Model (POM) design pattern. The suite currently covers the main checkout journey and additional negative scenarios for login, catalog, product detail, and cart behavior.

## Target Application

- App package: `com.saucelabs.mydemoapp.rn`
- App activity: `com.saucelabs.mydemoapp.rn.MainActivity`
- Mobile automation framework: Appium
- Driver: AndroidDriver with UiAutomator2
- Test runner: TestNG
- Assertions: TestNG + AssertJ

## Technology Stack

- Java 17
- Maven
- Appium Java Client
- Selenium / Appium WebDriver APIs
- TestNG
- AssertJ
- Android Emulator / Real Device

## Project Structure

```text
mobile/
├── pom.xml
├── README.md
├── .idea/
│   ├── deviceManager.xml
│   ├── misc.xml
│   ├── modules.xml
│   ├── vcs.xml
│   ├── workspace.xml
│   └── caches/
│       └── deviceStreaming.xml
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── velocitor/
│   │   │           └── mobile/
│   │   │               ├── base/
│   │   │               │   ├── BaseMobileTest.java
│   │   │               │   └── DriverFactory.java
│   │   │               ├── pages/
│   │   │               │   ├── BasePage.java
│   │   │               │   ├── CartPage.java
│   │   │               │   ├── CheckoutPage.java
│   │   │               │   ├── HomePage.java
│   │   │               │   ├── LoginPage.java
│   │   │               │   ├── OrderCompletionPage.java
│   │   │               │   ├── PaymentPage.java
│   │   │               │   ├── ProductCatalogPage.java
│   │   │               │   ├── ProductDetailsPage.java
│   │   │               │   └── ReviewOrderPage.java
│   │   │               └── tests/
│   │   │                   ├── CartAndProductDetailNegativeTests.java
│   │   │                   ├── CheckoutJourneyTest.java
│   │   │                   └── LoginNegativeTests.java
│   │   └── resources/
│   │       └── apps/
│   │           └── MyDemoApp.apk
└── target/
    ├── generated-test-sources/
    ├── maven-status/
    ├── surefire-reports/
    └── test-classes/
```

## Key Components

### 1. DriverFactory
`DriverFactory.java` is responsible for creating the Appium AndroidDriver session.

Responsibilities:
- set default Appium server URL
- set device name and Android version
- configure app path and app package/activity
- enable permissions
- set command timeout and automation name
- create the AndroidDriver instance

This class centralizes the app and device configuration so the tests remain environment independent.

### 2. BaseMobileTest
`BaseMobileTest.java` serves as the shared base class for all test classes.

Responsibilities:
- initialize the Appium driver before each test
- dismiss Android compatibility popups if they appear
- close the driver after each test
- expose the shared mobile driver

This makes each test isolated and reduces duplicate setup code.

### 3. BasePage
`BasePage.java` provides common UI interaction utilities.

Responsibilities:
- WebDriverWait setup
- generic visibility checks
- reusable `waitVisible(...)` methods
- helper methods used by all page objects

This reduces duplication across different screens and keeps page classes cleaner.

### 4. Page Object Classes
Each app screen has its own `Page Object` class.

- `HomePage`
  - home screen
  - menu open actions
  - login entry
  - product catalog navigation

- `LoginPage`
  - username/password fields
  - login button
  - validation messages

- `ProductCatalogPage`
  - visible product list
  - open selected product
  - cart navigation

- `ProductDetailsPage`
  - product title/price
  - quantity adjustments
  - color selection
  - add to cart

- `CartPage`
  - cart state
  - item list
  - total items/price
  - proceed to checkout

- `CheckoutPage`
  - shipping address details
  - navigation to payment

- `PaymentPage`
  - card details
  - billing address checks
  - review order flow

- `ReviewOrderPage`
  - review selected product and totals
  - place order action

- `OrderCompletionPage`
  - final confirmation screen
  - verify order completed successfully

### 5. Test Classes
`CheckoutJourneyTest.java` contains the main end-to-end workflow.

This is the primary test flow:
1. app opens
2. open menu
3. login
4. select a product
5. open product details
6. add to cart
7. open cart
8. proceed to checkout
9. fill shipping address
10. go to payment
11. fill payment details
12. review order
13. place order
14. confirm order completion

## Design

This project follows a clean and maintainable mobile test automation design.

### Page Object Model (POM)
Every screen is represented by a dedicated page class. This keeps:
- locators in one place
- test methods readable
- UI changes isolated to screen objects
- the automation resilient to app UI changes

### Shared Driver Model
The Appium driver is created once per test in `BaseMobileTest` and used by all page objects. This keeps the flow consistent and reduces driver creation noise.

### Wait Strategy
The framework uses explicit waits through `WebDriverWait` to avoid flaky tests caused by timing issues. This is important for dynamic native Android app screens.

### Screen Isolation
Each page object encapsulates:
- UI element locators
- interaction methods
- validation methods

This way, tests focus on business behavior instead of low-level element operations.

### Config-Driven Setup
Driver setup is centralized in `DriverFactory`, so environment-specific configuration such as:
- App path
- device name
- Android version
- Appium host
can be controlled externally or through Maven system properties.

## Supported Flow

The current automation covers this business flow:

```text
Home page
 -> Menu
 -> Login
 -> Product catalog
 -> Select product
 -> Product details
 -> Add to cart
 -> Cart
 -> Checkout
 -> Payment
 -> Review order
 -> Place order
 -> Order completion
```

## Prerequisites

Before running the automation suite, make sure:

- Java 17 is installed
- Maven is installed
- Android emulator or physical device is available
- Appium server is running
- APK is present in `src/test/resources/apps/`

## Appium Setup

Start Appium:

```bash
appium
```

Default Appium URL used by the project:

```text
http://127.0.0.1:4723
```

## Run the Test

From the project root:

```bash
cd c:\sks\mobile-automation\mobile
mvn test -Dtest=CheckoutJourneyTest
```

Optional runtime overrides:

```bash
mvn test -Dtest=CheckoutJourneyTest
    -Dapp.path="C:\sks\mobile-automation\mobile\src\test\resources\apps\MyDemoApp.apk"
    -Ddevice.name="Pixel_9_Pro"
    -Dplatform.version="17"
```

## Handling App Launch Popups

Some Android environments show an app compatibility dialog when the app starts. The project handles this in `BaseMobileTest` by checking for common Android popup button IDs and closing the dialog automatically before continuing.

## Current State

The project currently includes:
- core Appium driver configuration
- page object model for each screen
- end-to-end checkout automation
- reusable UI utilities
- mobile test structure ready for execution on Android

This suite is structured to support continuous mobile UI automation and is aligned with the current app flow and page model used in the My Demo App.
