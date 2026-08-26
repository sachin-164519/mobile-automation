# Mobile Suite Design — Sauce Labs "My Demo App" (Android)

## Target app

Sauce Labs **My Demo App** (Android), built from the APK on the project's
[releases page](https://github.com/saucelabs/my-demo-app-android/releases).
It's the mobile counterpart to Sauce Demo (`saucedemo.com`): a login screen,
a product catalog, a cart, and a checkout flow, using the same
`standard_user` / `secret_sauce` style credentials and the same general
screen flow.

## Structure

```
mobile/
├── pom.xml
├── DESIGN.md
└── src/test/java/com/velocitor/mobile/
    ├── base/
    │   ├── DriverFactory.java     # builds the AndroidDriver from configurable capabilities
    │   └── BaseMobileTest.java    # per-test app launch/teardown (JUnit 5 lifecycle)
    ├── pages/
    │   ├── BasePage.java          # shared explicit-wait helpers
    │   ├── LoginPage.java
    │   ├── ProductCatalogPage.java
    │   ├── CartPage.java
    │   └── CheckoutPage.java
    └── tests/
        ├── LoginTests.java        # valid + invalid login
        ├── CartTests.java         # add to cart, verify cart contents
        └── CheckoutTests.java     # end-to-end purchase flow
```

## Key design choices

**Page Object Model, one class per screen.** Each page object owns its own
locators and exposes intent-level methods (`login(user, pass)`,
`addFirstProductToCart()`, `proceedToCheckout()`) rather than exposing raw
`WebElement`s to tests. Test classes read like a script of user actions, not
a list of low-level driver calls — the same reasoning behind keeping the API
suite's request-building out of the test bodies.

**Locator strategy: accessibility id first.** Android content-descriptions
(exposed to Appium as accessibility ids) are the most stable selector this
app family uses — they don't shift with layout/resource-id changes the way
`R.id` values can across builds, and they're the same convention used in
Sauce Labs' own sample test suites for this app family (confirmed against
their `sample-app-mobile` reference tests: `test-Username`, `test-Password`,
`test-LOGIN`, `test-Error message`, `test-PRODUCTS`, etc.). Where I couldn't
independently verify an exact id (cart/checkout locators, in particular), I
used my best-informed guess following that same `test-`-prefixed convention
and flagged it in the page object's Javadoc rather than presenting it as
verified. In a real onboarding, the very first thing I'd do is open Appium
Inspector against the actual APK and correct/confirm every locator against
the live element tree — that's a five-minute task with a running emulator
and not something worth faking confidence about without one.

**Fresh app session per test.** `BaseMobileTest` launches a new driver
session in `@BeforeEach` and quits it in `@AfterEach`, the same independence
requirement the assignment calls out for the API suite. It costs time
(app relaunch is not free), but a test that adds an item to a cart shouldn't
leave state for the next test to trip over, and debugging a failure is much
easier when you know the app started from a known, empty state.

**Explicit waits everywhere, no `Thread.sleep`.** `BasePage` wraps
`WebDriverWait` + `ExpectedConditions`. Emulators are slow and inconsistent;
hardcoded sleeps are one of the most common sources of flaky mobile suites —
tuned once against a fast machine, they start failing (or start silently
wasting minutes) as soon as the environment changes.

**Config mirrors the API suite's pattern.** `DriverFactory` reads the Appium
server URL, device name, platform version, and app path from system
properties with sane local-emulator defaults, the same "configurable, not
hardcoded" principle the assignment requires for the API suite's base URI.
Swapping these for a device-cloud provider's capabilities (see below) is a
config change, not a test-code change.

## Running locally

Requires a running Appium 2 server and a booted Android emulator (or
connected device) with the My Demo App APK downloaded locally.

```bash
appium                     # start the Appium server (default port 4723)
cd mobile
mvn test \
  -Dapp.path=/absolute/path/to/Android-MyDemoApp.apk \
  -Ddevice.name=Pixel_6_API_34 \
  -Dplatform.version=14
```

I did not have an emulator set up to execute this suite end-to-end for
submission — per the assignment, this part is graded on design and a
representative sample rather than a fully green run. The code is written to
actually compile and run against a real session; it isn't pseudocode.

## What I'd do with more time

- Verify every locator against the real element tree via Appium Inspector,
  and switch `addFirstProductToCart()` to target a specific named product
  (e.g., by locating the name text, then its sibling "ADD TO CART" button)
  instead of "whichever is first."
- Add negative/edge coverage mirroring the API suite's adversarial style:
  removing an item from the cart, checkout with missing required fields,
  back-navigation mid-checkout, app backgrounding/resume during checkout.
- Add a screenshot-on-failure JUnit 5 extension — invaluable for mobile
  specifically, since a CI failure log rarely tells you *what the screen
  actually looked like* the way a screenshot does.
- Consider `Appium 2`'s parallel session support once the suite grows past
  a handful of tests, so mobile runs don't become the slow leg of CI.

## Running mobile tests in CI: emulator vs. device cloud

Running an Android emulator directly on a GitHub-hosted runner is possible
(`reactivecircus/android-emulator-runner` is the standard action for it) but
it's slow to boot, resource-constrained, and only ever tests one OS/device
combination. For anything beyond a smoke check, I'd point CI at a device
cloud (Sauce Labs' own device cloud, or BrowserStack App Automate) instead:
upload the APK, swap `DriverFactory`'s capabilities for the provider's
remote WebDriver URL + device matrix, and get real-device coverage across
OS versions without maintaining emulator infrastructure. See the root
`README.md`'s CI section for how that's wired as a separate, mostly-stubbed
job in `.github/workflows/ci.yml`.
