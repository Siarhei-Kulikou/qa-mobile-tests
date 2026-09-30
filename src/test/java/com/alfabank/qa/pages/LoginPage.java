package com.alfabank.qa.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * Page Object экрана «Вход в Alfa-Test».
 */
public class LoginPage extends BasePage {

    // XPath
    private final By title = By.xpath("//android.widget.TextView[@resource-id='" + ID_PREFIX + "tvTitle']");
    private final By passwordInput = By.xpath("//android.widget.EditText[@resource-id='" + ID_PREFIX + "etPassword']");

    // UiSelector с регулярным выражением (textMatches)
    private final By titleByRegex = AppiumBy.androidUIAutomator(
            "new UiSelector().textMatches(\"^Вход в Alfa-Test$\")");

    // resource-id
    private final By usernameInput = By.id(ID_PREFIX + "etUsername");
    private final By passwordToggle = By.id(ID_PREFIX + "text_input_end_icon");
    private final By loginButton = By.id(ID_PREFIX + "btnConfirm");
    private final By errorText = By.id(ID_PREFIX + "tvError");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    @Step("Дождаться открытия экрана входа")
    public LoginPage waitUntilOpened() {
        waitVisible(title);
        return this;
    }

    public boolean isOpened() {
        return !driver.findElements(titleByRegex).isEmpty();
    }

    @Step("Ввести логин: {0}")
    public LoginPage typeUsername(String username) {
        WebElement field = waitVisible(usernameInput);
        field.click();
        field.sendKeys(username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage typePassword(String password) {
        WebElement field = waitVisible(passwordInput);
        field.click();
        field.sendKeys(password);
        return this;
    }

    @Step("Нажать кнопку «Вход»")
    public void tapLogin() {
        hideKeyboardIfShown();
        waitVisible(loginButton).click();
    }

    @Step("Войти под пользователем {0}")
    public MainPage loginAs(String username, String password) {
        typeUsername(username);
        typePassword(password);
        tapLogin();
        return new MainPage(driver);
    }

    @Step("Нажать на значок «глаз» у поля пароля")
    public LoginPage togglePasswordVisibility() {
        waitVisible(passwordToggle).click();
        return this;
    }

    /** Ждёт появления непустого сообщения об ошибке и возвращает его текст. */
    @Step("Получить текст сообщения об ошибке")
    public String waitForError() {
        return wait.until(d -> {
            String text = d.findElement(errorText).getText();
            return text == null || text.isBlank() ? null : text;
        });
    }

    /** Текст поля логина. Пока поле пустое, Android возвращает подсказку «Логин». */
    public String getUsernameText() {
        return waitVisible(usernameInput).getText();
    }

    /** Текст поля пароля (в замаскированном виде должен состоять из символов «•»). */
    public String getPasswordText() {
        return waitVisible(passwordInput).getText();
    }

    /** true, если пароль сейчас скрыт (атрибут password у EditText). */
    public boolean isPasswordMasked() {
        return Boolean.parseBoolean(waitVisible(passwordInput).getAttribute("password"));
    }

    private void hideKeyboardIfShown() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (WebDriverException ignored) {
            // клавиатура не мешает, идём дальше
        }
    }
}
