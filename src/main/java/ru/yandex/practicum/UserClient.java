package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.UserCredentials;
import ru.yandex.practicum.model.UserProfile;
import ru.yandex.practicum.model.UserToken;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;


public class UserClient extends BaseApiClient {

    public static int WAIT_TIME = 1;

    // Периодически тесты падали с ошибкой "429 Too Many Requests",
    // в связи с этим добавлен таймаут

    @Step("Регистрация пользователя")
    public static Response registerUser(User user){
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}

        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register/");

    }
    @Step("Залогинивание пользователя")
    public static Response loginUser(UserCredentials userCredentials) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post(BASE_URL + "/api/auth/login/");

    }

    @Step("Выход пользователя")
    public static Response logoutUser(UserToken userToken) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(userToken)
                .when()
                .post(BASE_URL + "/api/auth/logout/");

    }

    @Step("Удаление пользователя")
    public static Boolean deleteUser(UserToken userToken) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}
        return given()
                .spec(getRecSpec())
                .header("Authorization", userToken.getAccessToken())
                .when()
                .delete(BASE_URL + "/api/auth/user/")
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("success");
    }
    @Step("Изменение данных авторизованного пользователя")
    public static Response changeUserAuthorized(UserToken userToken, UserProfile userProfile) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}
        return given()
                .spec(getRecSpec())
                .header("Authorization", userToken.getAccessToken())
                .body(userProfile)
                .when()
                .patch(BASE_URL + "/api/auth/user ");
    }
    @Step("Изменение данных не авторизованного пользователя")
    public static Response changeUserUnauthorized(UserProfile userProfile) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {}
        return given()
                .spec(getRecSpec())
                .body(userProfile)
                .when()
                .patch(BASE_URL + "/api/auth/user ");
    }
}

