package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.UserCredentials;
import ru.yandex.practicum.model.UserProfile;
import ru.yandex.practicum.model.UserToken;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;


public class UserClient extends BaseApiClient {
    // Периодически тесты падали с ошибкой "429 Too Many Requests",
    @Step("Регистрация пользователя")
    public static Response registerUser(User user) {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register/");

    }

    @Step("Залогинивание пользователя")
    public static Response loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post("/api/auth/login/");

    }

    @Step("Выход пользователя")
    public static Response logoutUser(UserToken userToken) {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(userToken)
                .when()
                .post("/api/auth/logout/");

    }

    @Step("Удаление пользователя")
    public static Boolean deleteUser(UserToken userToken) {
        return given()
                .spec(getRecSpec())
                .header("Authorization", userToken.getAccessToken())
                .when()
                .delete("/api/auth/user/")
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("success");
    }

    @Step("Изменение данных авторизованного пользователя")
    public static Response changeUserAuthorized(UserToken userToken, UserProfile userProfile) {
        return given()
                .spec(getRecSpec())
                .header("Authorization", userToken.getAccessToken())
                .body(userProfile)
                .when()
                .patch("/api/auth/user ");
    }

    @Step("Изменение данных не авторизованного пользователя")
    public static Response changeUserUnauthorized(UserProfile userProfile) {
        return given()
                .spec(getRecSpec())
                .body(userProfile)
                .when()
                .patch("/api/auth/user ");
    }
}