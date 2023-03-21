package usertests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.getUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginWithValidCredTest() {
        ValidatableResponse loginResponse = userClient.loginUser(user).statusCode(200);
        loginResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с некорректным email")
    public void loginIncorrectEmailTest() {
        user.setEmail(user.getEmail() + "Саша");
        ValidatableResponse loginIncEmailResponse = userClient.loginUser(user).statusCode(401);
        loginIncEmailResponse.assertThat().body("success", equalTo(false));
        loginIncEmailResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с некорректным пароля")
    public void loginIncorrectPasswordTest() {
        user.setPassword(user.getPassword() + "Саша");
        ValidatableResponse loginIncPasswordResponse = userClient.loginUser(user).statusCode(401);
        loginIncPasswordResponse.assertThat().body("success", equalTo(false));
        loginIncPasswordResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация при указании некорректного email и password")
    public void loginWithIncorrectCredTest() {
        user.setEmail(user.getEmail() + "Саша");
        user.setPassword(user.getPassword() + "Саша");
        ValidatableResponse loginIncCredResponse = userClient.loginUser(user).statusCode(401);
        loginIncCredResponse.assertThat().body("success", equalTo(false));
        loginIncCredResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
