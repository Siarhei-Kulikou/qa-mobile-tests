package com.alfabank.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Настройки запуска. Значения берутся из config.properties,
 * их можно переопределить через -D параметры (например, -Dappium.url=...).
 */
public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }
}
