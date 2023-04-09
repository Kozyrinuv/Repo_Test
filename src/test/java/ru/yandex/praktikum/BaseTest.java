package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.praktikum.pageobjects.*;

import java.util.concurrent.TimeUnit;

import static ru.yandex.praktikum.PathConst.DRIVER_CHROME;
import static ru.yandex.praktikum.PathConst.MAIN_PAGE;

public class BaseTest {
    private final UserClient userClient = new UserClient();
    private final UserAccountData userAccountData = new UserAccountData();
    private final String userLogin = userAccountData.getLoginJson();
    private final String userPassword = userAccountData.getPasswordJson();
    private final String userName = userAccountData.getNameJson();
    protected LoginPage loginPage;
    protected ConstructorPage constructorPage;
    protected ProfilePage profilePage;
    protected RegisterPage registerPage;
    protected RecoveryPage recoveryPage;
    protected WebDriver driver;
    private String token;

    @Before
    @DisplayName("Подключаем браузер и создаем объекты PageObject")
    public void setUp() {
        if (DRIVER_CHROME) {
            driver = new ChromeDriver();
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setBinary(System.getProperty("user.home") + "\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");
            driver = new ChromeDriver(chromeOptions);
        }
        driver.manage().window().maximize();
        //Создаем пользователя
        RestAssured.baseURI = MAIN_PAGE;
        token = null;
        String json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        token = userClient.createUser(json);
    }

    @After
    public void teardown() {
        timesleep(10);
        driver.quit();
        //Удаляем созданного пользователя
        if (token != null) userClient.deleteUser(token.substring(7));
    }

    //Задержка для визуализации работы тестов
    public void timesleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
