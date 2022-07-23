package ru.yandex.practicum;

import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.*;
import static ru.yandex.practicum.OrderClient.*;
import static ru.yandex.practicum.UserClient.*;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import ru.yandex.practicum.model.*;

import java.util.ArrayList;
import java.util.List;


@Story("Тестирование API для работы с заказами")
public class CreateOrderTest {
    public User user;
    public UserCredentials userCredentials;
    public UserProfile userProfile;
    public UserToken userToken;
    public Response responseUser;
    public Response orderUser;
    public Response ordersUser;
    public Boolean deleteUser = true;
    public Order order;
    public List<Ingredient> ingredientList;

    @Before
    public void beforeTests() {
        RestAssured.baseURI = OrderClient.BASE_URL;

        ingredientList = new ArrayList<>();
        for (Object obj : GetIngredients()) {
            ingredientList.add(new Ingredient(obj.toString(), ""));
        }
        order = new Order(ingredientList);
    }

    @After
    public void afterTests() {
        if (deleteUser) {
            deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - создается корректный заказ с набором ингредиентов\n" +
            " - в заказ добавляются все возможные ингредиенты и заказ успешно создается, это выглядит некорректным поведением API")
    public void orderAuthUser() {
        user = User.getRandomUser();
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        orderUser = CreateOrder(order);
        CreateOrderResponse createOrderResponse = orderUser.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.success);
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - не создается корректный заказ с набором ингредиентов\n" +
            " - в текущей реализации через API создается неавторизованный заказ и тест не проходит")
    public void orderNotAuthUser() {
        deleteUser = false;
        orderUser = CreateOrder(order);
        CreateOrderResponse createOrderResponse = orderUser.as(CreateOrderResponse.class);
        assertFalse(createOrderResponse.success);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - не создается корректный заказ без набора ингредиентов")
    public void orderAuthUserNotIngredient() {
        user = User.getRandomUser();
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        order = new Order(new ArrayList<>());
        orderUser = CreateOrder(order);
        CreateOrderResponse createOrderResponse = orderUser.as(CreateOrderResponse.class);
        assertFalse(createOrderResponse.success);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов не авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - не создается корректный заказ без набора ингредиентов")
    public void orderNotAuthUserNotIngredient() {
        deleteUser = false;
        order = new Order(new ArrayList<>());
        orderUser = CreateOrder(order);
        CreateOrderResponse createOrderResponse = orderUser.as(CreateOrderResponse.class);
        assertFalse(createOrderResponse.success);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неверным хешем ингредиентов")
    @Description("Проверяем:\n" +
            " - не создается корректный заказ с неверным хешем ингредиентов")
    public void orderAuthUserErrorIngredient() {
        user = User.getRandomUser();
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        order.ingredients.set(0, new Ingredient(order.ingredients.get(0).get_id() + "1", ""));
        orderUser = CreateOrder(order);
        assertTrue(orderUser.statusCode() == SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем с неверным хешем ингредиентов")
    @Description("Проверяем:\n" +
            " - не создается корректный заказ с неверным хешем ингредиентов не авторизованным пользователем")
    public void orderNotAuthUserErrorIngredient() {
        deleteUser = false;
        order.ingredients.set(0, new Ingredient(order.ingredients.get(0).get_id() + "1", ""));
        orderUser = CreateOrder(order);
        assertTrue(orderUser.statusCode() == SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - получены заказы конерктного пользователя, а не общие\n" +
            " - в текущей реализации через API всегда возвращается общий список заказов, тест не проходит")
    public void GetOrderAuthUser() {
        user = User.getRandomUser();
        responseUser = registerUser(user);
        CreateUserResponse createUserResponse = responseUser.as(CreateUserResponse.class);
        userToken = responseUser.as(UserToken.class);
        assertTrue(createUserResponse.success);
        orderUser = CreateOrder(order);
        CreateOrderResponse createOrderResponse = orderUser.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.success);
        ordersUser = GetOrder(userToken);
        GetOrdersResponse getOrdersResponse = ordersUser.as(GetOrdersResponse.class);
        assertTrue(getOrdersResponse.success);
        assertTrue(getOrdersResponse.total == 1);
    }

    @Test
    @DisplayName("Получение заказа не авторизованным пользователем")
    @Description("Проверяем:\n" +
            " - не получены заказы")
    public void GetOrderNotAuthUser() {
        deleteUser = false;
        ordersUser = GetNotAuthOrder();
        GetOrdersResponse getOrdersResponse = ordersUser.as(GetOrdersResponse.class);
        assertFalse(getOrdersResponse.success);
    }
}