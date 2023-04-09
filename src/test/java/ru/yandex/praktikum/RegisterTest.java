package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.pageobjects.RegisterPage;

//Тесты страницы "Регистрация" сайта "Звездные бургеры"
public class RegisterTest extends BaseTest {
    private final UserAccountData user = new UserAccountData();

    @Test
    @DisplayName("Успешная <Регистрация> нового пользователя")
    @Description("Проверка <Регистрации> нового пользователя на странице <Регистрация>")
    public void checkRegistrationOKTest() {
        String name = "xxx" + user.getName();
        String email = "xxx" + user.getEmail();
        String password = "xxx" + user.getPassword();
        registerPage = new RegisterPage(driver);
        registerPage.open();
        registerPage.inputName(name);
        registerPage.inputEmail(email);
        registerPage.inputPassword(password);
        loginPage = registerPage.pressButtonRegistration();
        loginPage.waitClicableSiginButton();
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        constructorPage = loginPage.pressSiginButton();
        constructorPage.checkClickableMakeOrderButton();
        //Получаем token вновь зарегистрированного пользователя для последующего удаления
        UserClient userClient = new UserClient();
        String token = null;
        String json = "{" + "\"email\": \"" + email + "\"," + "\"password\": \"" + password + "\"}";
        token = userClient.loginExistingUser(json);
        //Удаление вноь зарегистрированного пользователя
        if (token != null) userClient.deleteUser(token.substring(7));
    }

    @Test
    @DisplayName("Некорректная  <Регистрация> нового пользователя")
    @Description("Проверка <Регистрации> нового пользователя на странице <Регистрация> при длине пароля менее 6 символов")
    public void heckRegistrationWhithIncorrectPasswordTest() {
        registerPage = new RegisterPage(driver);
        registerPage.open();
        registerPage.inputName(user.getName());
        registerPage.inputEmail(user.getEmail());
        registerPage.inputPassword(user.getPasswordIncorrect());
        registerPage.pressButtonRegistration();
        registerPage.checkErrorMessage();
    }

}
