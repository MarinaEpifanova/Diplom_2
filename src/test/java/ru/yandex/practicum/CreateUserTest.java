package ru.yandex.practicum;

import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static ru.yandex.practicum.UserClient.*;
import static ru.yandex.practicum.UserClient.loginUser;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import ru.yandex.practicum.model.*;


@Story("Тестирование API для работы с пользователем")
public class CreateUserTest {

    public User user;
    public UserCredentials userCredentials;
    public UserProfile userProfile;
    public UserToken userToken;
    public Response responseUser;
    public Boolean deleteUser;

    @Before
    public void beforeTests() {
        RestAssured.baseURI = UserClient.BASE_URL;
        deleteUser = true;
        user = User.getRandomUser();
    }

    @After
    public void afterTests() {
        if (deleteUser) {
            deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя при пустом email")
    @Description("Проверяем:\n" +
            " - если email нет, пользователь не создается")
    public void userTestMissingEmail() {
        deleteUser = false;
        user.setEmail("");
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Создание пользователя при пустом пароле")
    @Description("Проверяем:\n" +
            " - если пароля нет, пользователь не создается")
    public void userTestMissingPassword() {
        deleteUser = false;
        user.setPassword("");
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Создание пользователя при пустом имени")
    @Description("Проверяем:\n" +
            " - если имени нет, пользователь не создается")
    public void userTestMissingName() {
        deleteUser = false;
        user.setName("");
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверяем:\n" +
            " - корректные входные данные, пользователь успешно создается")
    public void userCorrectRegister() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Создание пользователя,который уже существует")
    @Description("Проверяем:\n" +
            " - корректные входные данные, пользователь повторно не создается")
    public void userDoubleRegister() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        logoutUser(userToken);
        responseUser = registerUser(user);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверяем:\n" +
            " - корректные входные данные, пользователь успешно авторизовался")
    public void userLogin() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        logoutUser(userToken);
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        responseUser = loginUser(userCredentials);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверяем:\n" +
            " - не корректные входные данные, пользователь не авторизовался")
    public void userIncorrectLogin() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        logoutUser(userToken);
        userCredentials = new UserCredentials(user.getEmail() + "abc", user.getPassword());
        responseUser = loginUser(userCredentials);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверяем:\n" +
            " - не корректные входные данные, пользователь не авторизовался")
    public void userIncorrectPassword() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        logoutUser(userToken);
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword() + "abc");
        responseUser = loginUser(userCredentials);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Изменение имени авторизованого пользователя")
    @Description("Проверяем:\n" +
            " - имя можно изменить")
    public void userChangeName() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userProfile = new UserProfile(user.getEmail(), user.getName());
        createUserResponse = changeUserAuthorized(userToken, userProfile).as(CreateUserResponse.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Изменение почты авторизованого пользователя")
    @Description("Проверяем:\n" +
            " - почту можно изменить")
    public void userChangeEmail() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userProfile = new UserProfile(user.getEmail(), user.getName());
        createUserResponse = changeUserAuthorized(userToken, userProfile).as(CreateUserResponse.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Изменение почты не авторизованого пользователя")
    @Description("Проверяем:\n" +
            " - почту нельзя изменить")
    public void LogoutUserChangeEmail() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        userProfile = new UserProfile(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", user.getName());
        responseUser = changeUserUnauthorized(userProfile);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Изменение имени не авторизованого пользователя")
    @Description("Проверяем:\n" +
            " - имя нельзя изменить")
    public void LogoutUserChangeName() {
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        userProfile = new UserProfile(user.getEmail(), RandomStringUtils.randomAlphabetic(10));
        responseUser = changeUserUnauthorized(userProfile);
        createUserResponse = responseUser.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }
}