package com.alfabank.qa.tests;

import com.alfabank.qa.base.BaseTest;
import com.alfabank.qa.pages.LoginPage;
import com.alfabank.qa.pages.MainPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("Экран «Вход в Alfa-Test»")
public class LoginTest extends BaseTest {

    private static final String VALID_LOGIN = "Login";
    private static final String VALID_PASSWORD = "Password";

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void openLoginScreen() {
        loginPage = new LoginPage(driver).waitUntilOpened();
    }

    @Test(description = "1. Успешный вход с корректными логином и паролем")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login/Password -> переход на экран «Вход в Alfa-Test выполнен»")
    public void successfulLogin() {
        MainPage mainPage = loginPage.loginAs(VALID_LOGIN, VALID_PASSWORD);

        assertThat(mainPage.isOpened())
                .as("Открылся экран успешного входа")
                .isTrue();
        assertThat(mainPage.getTitleText())
                .as("Текст на экране успешного входа")
                .matches("^Вход в Alfa-Test выполнен$");
    }

    @Test(description = "2. Вход с неверным паролем показывает сообщение об ошибке")
    @Severity(SeverityLevel.CRITICAL)
    public void wrongPasswordShowsError() {
        loginPage.typeUsername(VALID_LOGIN)
                .typePassword("WrongPassword")
                .tapLogin();

        assertThat(loginPage.waitForError())
                .as("Сообщение об ошибке")
                .matches("^Введены неверные данные$");
        assertThat(loginPage.isOpened())
                .as("Пользователь остался на экране входа")
                .isTrue();
    }

    @Test(description = "3. Пароль маскируется символом «•», значок «глаз» переключает видимость")
    @Severity(SeverityLevel.NORMAL)
    public void passwordIsMaskedAndToggles() {
        loginPage.typePassword(VALID_PASSWORD);

        assertThat(loginPage.isPasswordMasked()).as("По умолчанию пароль скрыт").isTrue();

        loginPage.togglePasswordVisibility(); // нечётное нажатие
        assertThat(loginPage.isPasswordMasked()).as("После 1-го нажатия пароль виден").isFalse();
        assertThat(loginPage.getPasswordText()).as("Виден введённый пароль").isEqualTo(VALID_PASSWORD);

        loginPage.togglePasswordVisibility(); // чётное нажатие
        assertThat(loginPage.isPasswordMasked()).as("После 2-го нажатия пароль снова скрыт").isTrue();
    }

    @Test(description = "4. Пустые поля: должны показываться валидационные сообщения")
    @Severity(SeverityLevel.NORMAL)
    @Description("По требованиям при пустых полях выводятся валидационные сообщения (Login-1, Pass-1). "
            + "В приложении показывается общее «Введены неверные данные». Ожидаемо падает: дефект.")
    public void emptyFieldsShowValidationMessage() {
        loginPage.tapLogin();

        assertThat(loginPage.waitForError())
                .as("Валидационное сообщение по пустому полю, а не ошибка учётных данных")
                .doesNotMatch("^Введены неверные данные$");
    }

    @Test(description = "5. В поле логина нельзя ввести более 50 символов")
    @Severity(SeverityLevel.NORMAL)
    @Description("По требованиям максимум 50 символов. В приложении ограничения ввода нет. "
            + "Ожидаемо падает: дефект.")
    public void loginFieldLimitedTo50Chars() {
        loginPage.typeUsername("a".repeat(51));

        assertThat(loginPage.getUsernameText().length())
                .as("Длина значения в поле логина")
                .isLessThanOrEqualTo(50);
    }
}
