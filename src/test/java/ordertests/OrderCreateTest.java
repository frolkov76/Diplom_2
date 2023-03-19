package ordertests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Ingredients;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest {

    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private Ingredients ingredients;

    @Before
    public void setUp() {
        user = User.getUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");
        ingredients = Ingredients.getRandomBurger();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентом")
    public void createOrderWithAuthTest() {
        ValidatableResponse orderResponse = orderClient.createOrderWithToken(accessToken.substring(7), Ingredients.getRandomBurger()).statusCode(200);
        orderResponse.assertThat().body("order.number", notNullValue());
        orderResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse orderResponse = orderClient.createOrderWithoutToken(Ingredients.getRandomBurger()).statusCode(200);
        orderResponse.assertThat().body("order.number", notNullValue());
        orderResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиента")
    public void createOrderWithoutIngredientTest() {
        ValidatableResponse orderResponse = orderClient.createOrderWithToken(accessToken.substring(7), Ingredients.getNullIngredients()).statusCode(400);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хэшем ингредиента")
    public void createOrderWithIncorrectIngredientTest() {
        ValidatableResponse orderIncorrectIngredientResponse = orderClient.createOrderWithToken(accessToken.substring(7), Ingredients.getIncorrectIngredients()).statusCode(500);
    }
}
