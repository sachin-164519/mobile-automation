package com.velocitor.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolves the base URI for the system under test.
 *
 * <p>Resolution order (first match wins):
 * <ol>
 *   <li>{@code -Dbase.uri=...} JVM system property</li>
 *   <li>{@code BASE_URI} environment variable</li>
 *   <li>{@code base.uri} key in {@code src/test/resources/config.properties}</li>
 *   <li>Hardcoded fallback (the public Restful Booker demo instance)</li>
 * </ol>
 *
 * <p>This lets the same test suite run unmodified against the public demo API,
 * a local Docker instance, or a per-environment URL in CI, satisfying the
 * assignment's "base URI must be configurable" requirement without hardcoding
 * it anywhere in test code.
 */
public final class TestConfig {

    private static final String DEFAULT_BASE_URI = "https://restful-booker.herokuapp.com";
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_KEY = "base.uri";

    // Resolved once per JVM. Tests are read-only with respect to this value,
    // so caching it does not compromise test independence.
    private static final String BASE_URI = resolveBaseUri();

    private TestConfig() {
        // static accessor only
    }

    public static String baseUri() {
        return BASE_URI;
    }

    private static String resolveBaseUri() {
        String fromSystemProperty = System.getProperty(CONFIG_KEY);
        if (isNotBlank(fromSystemProperty)) {
            return fromSystemProperty.trim();
        }

        String fromEnv = System.getenv("BASE_URI");
        if (isNotBlank(fromEnv)) {
            return fromEnv.trim();
        }

        String fromFile = readFromPropertiesFile();
        if (isNotBlank(fromFile)) {
            return fromFile.trim();
        }

        return DEFAULT_BASE_URI;
    }

    private static String readFromPropertiesFile() {
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                return null;
            }
            Properties props = new Properties();
            props.load(in);
            return props.getProperty(CONFIG_KEY);
        } catch (IOException e) {
            // Fall through to the next resolution step rather than failing test setup
            // over an optional, best-effort config file.
            return null;
        }
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
