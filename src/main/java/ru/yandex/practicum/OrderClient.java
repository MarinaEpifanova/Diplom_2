package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.model.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;


public class OrderClient extends BaseApiClient {

    public static int WAIT_TIME = 1;

    @Step ("Создать заказ")
    public static Response CreateOrder(Order order) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {
        }

        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders/");
    }
    @Step ("Добавить ингредиент")
    public static List<Object> GetIngredients() {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {
        }

        return given()
                .spec(getRecSpec())
                .when()
                .get(BASE_URL + "/api/ingredients/")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getList("data._id");
    }
    @Step ("Получить заказ")
    public static Response GetOrder(UserToken userToken) {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {
        }

        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .header("Authorization", userToken.getAccessToken())
                .when()
                .get(BASE_URL + "/api/orders/");
    }
    @Step ("Получить заказ неавторизованным пользователем")
    public static Response GetNotAuthOrder() {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException ex) {
        }

        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/api/orders/");
    }
}