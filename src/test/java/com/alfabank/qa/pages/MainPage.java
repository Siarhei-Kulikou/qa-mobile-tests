package com.alfabank.qa.pages;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

/**
 * Page Object экрана «Вход в Alfa-Test выполнен».
 */
public class MainPage extends BasePage {

    // XPath: у этого TextView нет resource-id
    private final By successTitle = By.xpath("//android.widget.TextView[contains(@text,'выполнен')]");

    public MainPage(AndroidDriver driver) {
        super(driver);
    }

    @Step("Проверить, что открыт экран успешного входа")
    public boolean isOpened() {
        try {
            waitVisible(successTitle);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getTitleText() {
        return waitVisible(successTitle).getText();
    }
}
