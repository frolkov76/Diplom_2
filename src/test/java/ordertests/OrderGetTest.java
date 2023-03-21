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

public class OrderGetTest {
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
        orderClient.createOrderWithToken(accessToken.substring(7), Ingredients.getRandomBurger()).statusCode(200);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthTest() {
        ValidatableResponse getOrderResponse = orderClient.getOrdersWithToken(accessToken.substring(7)).statusCode(200);
        getOrderResponse.assertThat().body("success", equalTo(true));
        getOrderResponse.assertThat().body("orders.number", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithoutAuthTest() {
        ValidatableResponse getOrderResponse = orderClient.getOrdersWithoutToken().statusCode(401);
        getOrderResponse.assertThat().body("success", equalTo(false));
        getOrderResponse.assertThat().body("message", equalTo("You should be authorised"));
    }
}
