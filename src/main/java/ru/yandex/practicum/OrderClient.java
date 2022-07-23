package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.model.*;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {

    @Step("Создать заказ")
    public static Response CreateOrder(Order order) {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/orders/");
    }

    @Step("Добавить ингредиент")
    public static List<Object> GetIngredients() {
        return given()
                .spec(getRecSpec())
                .when()
                .get("/api/ingredients/")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getList("data._id");
    }

    @Step("Получить заказ")
    public static Response GetOrder(UserToken userToken) {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .header("Authorization", userToken.getAccessToken())
                .when()
                .get("/api/orders/");
    }

    @Step("Получить заказ неавторизованным пользователем")
    public static Response GetNotAuthOrder() {
        return given()
                .spec(getRecSpec())
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders/");
    }
}