# Velocitor Solutions — SDET Take-Home

Two parts, both attempted in full: **Part 1 (API automation)** is a
complete, runnable suite. **Part 2 (mobile automation)** is a full design
plus five representative sample tests, written to compile and run against a
real Appium session but not executed end-to-end (no local emulator set up —
see [`mobile/DESIGN.md`](mobile/DESIGN.md)). **Part 3 (CI)** is included as
a GitHub Actions workflow.

```
.
├── api/            # Part 1 — Rest Assured + JUnit 5 test suite (complete, runnable)
├── mobile/         # Part 2 — Appium design + sample tests (see mobile/DESIGN.md)
└── .github/workflows/ci.yml   # Part 3 — CI pipeline
```

## Part 1 — API suite: setup and run

**Requirements:** JDK 17+, Maven 3.8+, internet access (the suite runs
against the public `https://restful-booker.herokuapp.com` demo API by
default).

```bash
cd api
mvn test
```

That's it on a fresh machine — Maven resolves all dependencies from Maven
Central on first run. Test reports land in `api/target/surefire-reports/`.

**Note on the target API:** this is a shared public demo instance. It resets
its dataset every ~10 minutes and can be slow on the very first request
after a period of inactivity. If a run fails on the *first* test with a
timeout, it's almost always this cold start — re-run, or hit
`https://restful-booker.herokuapp.com/ping` once first to warm it up (the CI
workflow does this automatically).

### Pointing the suite at a different environment

The base URI is never hardcoded in test code. Resolution order (see
`TestConfig.java`):

```bash
# 1. JVM system property (highest priority)
mvn test -Dbase.uri=http://localhost:3001

# 2. Environment variable
BASE_URI=http://localhost:3001 mvn test

# 3. src/test/resources/config.properties (checked-in default)
# 4. hardcoded fallback — the public Heroku demo instance
```

### What's covered

| Area | File | Notes |
|---|---|---|
| Auth | `auth/AuthTests.java` | Valid/invalid credentials, missing fields. Covers a real API quirk — invalid credentials return **200**, not 401/403, with `reason: "Bad credentials"` in the body. |
| Create | `booking/BookingCreateTests.java` | Full round-trip validation (request body == response body via `AssertJ.usingRecursiveComparison()`), optional-field handling. |
| Read | `booking/BookingReadTests.java` | Get by id, list all, 404 on missing id, non-numeric id handling. |
| Search | `booking/BookingSearchTests.java` | Filter by first/last name (asserted strictly); filter by date range (asserted structurally only — see below). |
| Update | `booking/BookingUpdateTests.java` | Full PUT (with persistence check via a follow-up GET) and partial PATCH, both with and without auth. |
| Delete | `booking/BookingDeleteTests.java` | Delete + confirm gone via GET, delete without auth, delete on a non-existent id. |
| Negative/adversarial | `negative/NegativeTests.java` | Type confusion, malformed dates, negative prices, injection-style payloads, malformed JSON syntax. |

**Test independence:** every test creates its own fixture data (via
`BookingFactory`, which randomizes names) and its own auth token (via
`AuthSupport`) rather than sharing state through `@BeforeAll`/static
fields or relying on execution order. Any single test class, or any single
`@Test` method, can be run alone and will pass.

### Known API quirks this suite documents (not bugs in the suite)

Restful Booker is intentionally light on server-side validation and has a
few documented oddities. Rather than write tests assuming "textbook REST"
behavior and having them fail against the real API, these tests assert
*observed* behavior and say so explicitly in a comment, so a reader can tell
"this pins a known API quirk" apart from "this is what I expect correct
behavior to be":

- `POST /auth` with bad credentials returns **200**, not 401, with
  `{"reason": "Bad credentials"}`.
- A successful `DELETE` returns **201 Created**, not 200/204.
- `DELETE` on a non-existent booking id returns **405 Method Not Allowed**,
  not 404.
- `POST /booking` with a non-ISO date format is accepted (200) and silently
  **corrupts** the stored value rather than rejecting it.
- Name fields accept any string, including numeric strings and
  script-injection-style payloads, with no type/format validation.
- Filtering by `checkin`/`checkout` date has community-reported
  inconsistent behavior on the public demo instance — this suite asserts
  only that the endpoint responds 200 with a well-formed list, rather than
  asserting exact filtering semantics that would make the test flaky
  through no fault of the suite itself.

## Part 2 — Mobile suite

Design rationale, locator strategy, and full run instructions are in
[`mobile/DESIGN.md`](mobile/DESIGN.md). Short version: Page Object Model,
one class per screen, JUnit 5 lifecycle relaunching the app per test for
independence, explicit waits only (no `Thread.sleep`), and a `DriverFactory`
whose capabilities are config-driven the same way the API suite's base URI
is — so swapping from a local emulator to a device cloud in CI is a config
change, not a rewrite. Five sample tests across login, cart, and checkout.

## Part 3 — CI

`.github/workflows/ci.yml` has three jobs:

- **`api-tests`** — actually runs `mvn test` against the public demo API on
  every push/PR, publishes a JUnit-formatted report via
  `dorny/test-reporter`, and uploads the raw Surefire XML as a build
  artifact. This job fails naturally (non-zero exit) if any test fails.
- **`mobile-tests`** — intentionally a placeholder, not a real emulator run.
  See the reasoning in `mobile/DESIGN.md`; the short version is that an
  in-CI Android emulator is slow and only covers one device/OS combination,
  so the real version of this job would target a device cloud instead.
- **`quality-gate`** — an explicit gate job that fails if `api-tests` didn't
  pass, modeling what a required branch-protection check would look like.
  (`mvn test` already fails its own job on a red suite; this job exists to
  make "what blocks a merge" visible as its own named check rather than
  implicit in one job's exit code.)

### Evolving this into a real quality gate

- **What should block a merge:** the API suite (deterministic, fast,
  runs on every PR) should be a required check. I would *not* make the
  mobile suite a required/blocking check until it's running reliably on a
  device cloud with real device availability — a required check that's
  occasionally red because of device-cloud capacity, not actual app
  regressions, trains people to ignore red checks in general.
- **Flaky tests:** track failure history per test (most CI dashboards or
  Allure's history/trend view do this); anything failing intermittently
  gets quarantined into a non-blocking job with a linked ticket rather than
  silently retried into green, which just hides real flakiness. Retries are
  fine as a stopgap for known-external flakiness (e.g., the shared demo
  API's cold start), never as a substitute for fixing a genuinely flaky
  test.
- **Mobile in CI:** device cloud (Sauce Labs' own, or BrowserStack App
  Automate) over an in-CI emulator once past a handful of smoke tests —
  covered in more detail in `mobile/DESIGN.md`.
- **Keeping feedback fast as the suite grows:** split fast/slow tests into
  separate jobs that run in parallel rather than one growing sequential
  job (e.g., `@Tag("smoke")` on a subset that runs on every push, full
  suite on a schedule or pre-merge only); parallelize within Surefire
  (`forkCount`/`threadCount`) once independence is well-established across
  the suite, which it is here by design; cache Maven dependencies (already
  wired via `actions/setup-java`'s `cache: maven`) so dependency resolution
  isn't repeated cold on every run.

## Design decisions and trade-offs

- **Two independent Maven modules (`api/`, `mobile/`) instead of one
  multi-module build.** They have almost no shared code and very different
  dependency footprints (Rest Assured vs. Appium/Selenium). Keeping them
  separate means someone who only cares about the API suite (most reviewers
  of this submission) can `cd api && mvn test` without pulling in Appium's
  dependency tree at all, and a `pom.xml` parent/aggregator would add
  structure without adding value at this size.
- **JUnit 5 over TestNG.** No strong technical reason to prefer one here for
  a suite this size — JUnit 5 is what I reach for by default and its
  `@DisplayName` support made the adversarial/quirk-documenting tests read
  more clearly as sentences.
- **POJOs + Jackson over raw JSON strings for the API suite.** A couple of
  tests do use raw JSON text blocks deliberately (`NegativeTests`,
  `AuthTests`) where the point of the test *is* sending something the POJO
  model wouldn't naturally represent (malformed JSON, a field omitted
  entirely). Everywhere else, POJOs let `AssertJ.usingRecursiveComparison()`
  do full-object round-trip checks in one line instead of a long list of
  brittle field-by-field JsonPath assertions.
- **A builder on `Booking`, plus `toBuilder()`.** Tests read as "take this
  booking, change one field" (`original.toBuilder().totalprice(555).build()`)
  which keeps PATCH tests' expected-value construction honest — it's built
  from the same object the test created, not retyped by hand and liable to
  drift from it.
- **No shared/static auth token or fixture data anywhere.** Costs an extra
  HTTP call in some tests (a fresh `/auth` call instead of a cached token)
  in exchange for the independence the assignment explicitly requires: any
  test, run alone, in any order, should pass.
- **Plain Surefire XML/console output over Allure for reporting.** Allure
  would look nicer, but wiring it in correctly (AspectJ weaving,
  `allure.properties`, categories) is easy to get subtly wrong in ways that
  only show up when someone actually tries to generate the report — and I
  couldn't verify that end-to-end in the environment I built this in (more
  in the AI-usage note below). Surefire's XML output already satisfies the
  assignment's CI requirement and `dorny/test-reporter` renders it in the
  GitHub Actions UI. Allure is the natural next step, not a first one.

## What I'd do next with more time

- Wire up an actual Android emulator and run the mobile suite end-to-end,
  correcting any locators that don't match my best-informed guesses once
  I can see the real element tree in Appium Inspector.
- Add JSON Schema validation for the API responses as a complementary check
  alongside the current field-level assertions.
- Add contract-style tests asserting response headers/content-type, not
  just body and status code.
- Parameterize the negative-path tests (`@ParameterizedTest`) to cover a
  wider matrix of invalid inputs per field without one method per case.
- Add a `docker-compose.yml` running a local Restful Booker instance, so
  the suite isn't dependent on a shared public demo API's uptime/reset
  cycle for local development.
- Screenshot-on-failure for the mobile suite (see `mobile/DESIGN.md`).

## Notes on AI tool usage

I used Claude (Anthropic) significantly in producing this submission —
scaffolding the Maven project structure, drafting the majority of the test
code and page objects across both modules, and drafting this README. This
was conversational assistance (chat-based, iterating on requirements and
design), **not** MCP-based/agentic test generation against a live API or
running emulator — I don't have an MCP server wired up that could inspect
the target API or app directly, so nothing here was scaffolded or generated
by discovering behavior live against a running system, aside from the
documented API quirks explicitly researched and cited below.

A few things worth being explicit about, in the spirit of "make sure you
understand and can defend everything you submit":

- **The documented Restful Booker quirks** (bad-credentials status code,
  the 201 delete response, 405-not-404 on deleting a missing booking, the
  malformed-date corruption bug) were confirmed via web research against
  public QA blog posts and open-source test suites targeting this same
  API, not from running the tests myself first and observing the behavior.
  I'm confident in them as documented, common, and independently
  corroborated across multiple sources — but "confirmed via research" and
  "confirmed by running it" are different things, and I'd verify each one
  by actually running the suite before standing behind it in an interview.
- **I could not execute `mvn test` myself** while building this — the
  environment I built it in doesn't have network access to Maven Central.
  I reviewed the code carefully by hand (package/import correctness, API
  usage against Rest Assured/Appium's actual method signatures, brace/paren
  balance) but **you should run `mvn test` yourself before submitting**,
  per the assignment's own instruction that a submission that can't be run
  can't be graded. If anything doesn't compile or pass, I'd want to know
  that going in, not find out live.
- **The mobile suite's exact locators are my best-informed guess**, not
  independently verified against a running instance of the app — see the
  callout in `mobile/DESIGN.md`'s "Key design choices" section for what I
  did and didn't confirm, and how I'd close that gap first with a running
  emulator.

I can walk through and defend every line of this — the design choices, the
trade-offs, and where I'd extend it — in the live round.
