# qa-mobile-tests

Автотесты авторизации для приложения Alfa-Test (https://github.com/lunin-vadim/qa-mobile).

Стек: Java 17, Appium (UiAutomator2), TestNG, AssertJ, Allure, Gradle.

## Структура
- `base/BaseTest` — создание и закрытие сессии Appium, скриншот при падении
- `pages/` — Page Object: `LoginPage`, `MainPage`
- `tests/LoginTest` — 5 тестов авторизации
- `config/Config` + `config.properties` — настройки запуска

## Запуск
1. Запустить эмулятор и установить приложение (`adb install -r app-debug.apk`).
2. Запустить сервер: `appium`.
3. `./gradlew test`
4. Отчёт: `allure serve build/allure-results`

Настройки можно переопределить: `./gradlew test -Dappium.url=http://127.0.0.1:4723 -Ddevice.name=emulator-5554`

## Локаторы
- XPath: заголовок, поле пароля, заголовок экрана успеха
- UiSelector + регулярное выражение (`textMatches`): проверка открытия экрана входа
- resource-id: остальные элементы
- CSS-локаторы применимы только к WebView; приложение нативное, WebView в нём нет

## Найденные расхождения с требованиями (тесты 4 и 5 падают намеренно)
- Пустые поля: вместо валидационных сообщений показывается «Введены неверные данные»
- Поле логина не ограничивает ввод 50 символами
- Кнопка называется «Вход», в требованиях — «Войти»
- Фильтрации недопустимых символов в логине нет (сообщения InvalidValue/ExceptValue отсутствуют)

## Подготовка окружения

1. Установить JDK 17, Node.js 22 (LTS), Android Studio (SDK и эмулятор).
2. Установить Appium и драйвер:
   `npm install -g appium && appium driver install uiautomator2`
3. Собрать приложение из https://github.com/lunin-vadim/qa-mobile:
   - в репозитории нет Gradle wrapper, нужен Gradle 7.4.2 (`gradle wrapper --gradle-version 7.4.2`);
   - в `LoginUseCase.kt` в ветке `else` опечатка `LoginResult.Error()-`, её нужно исправить на `LoginResult.Error()`, иначе проект не компилируется;
   - собрать APK: `./gradlew assembleDebug` (результат: `app/build/outputs/apk/debug/app-debug.apk`).
4. Создать эмулятор (например, Pixel 6, Android 14, образ arm64 для Apple Silicon), запустить его и установить APK: `adb install -r app-debug.apk`.
5. Запустить `appium` и затем `./gradlew test`.

## Результат прогона

3 теста проходят, 2 падают намеренно (дефекты приложения, см. выше). Allure-отчёт: `allure serve build/allure-results`.
