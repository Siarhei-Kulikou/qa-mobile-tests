package com.alfabank.qa.base;

import com.alfabank.qa.config.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Базовый класс тестов: перед каждым тестом создаёт новую сессию Appium
 * (приложение стартует с чистым состоянием), после теста закрывает её.
 */
public abstract class BaseTest {

    protected AndroidDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUpDriver() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName(Config.get("device.name"))
                .setAppPackage(Config.get("app.package"))
                .setAppActivity(Config.get("app.activity"))
                .setNoReset(false)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        driver = new AndroidDriver(new URL(Config.get("appium.url")), options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver(ITestResult result) {
        if (driver == null) {
            return;
        }
        try {
            if (!result.isSuccess()) {
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Скриншот при падении", "image/png",
                        new ByteArrayInputStream(screenshot), "png");
            }
        } finally {
            driver.quit();
        }
    }
}
