package usertests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserUpdateTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.getUser();
        userClient = new UserClient();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Изменение всех данных пользователя с авторизацией")
    public void updateAllUserDataWithAuthTest() {
        user.setEmail("test" + user.getEmail());
        user.setPassword("test" + user.getPassword());
        user.setName("test" + user.getName());
        ValidatableResponse updAllResponse = userClient.changeUserData(accessToken.substring(7), user).statusCode(200);
        updAllResponse.assertThat().body("success", equalTo(true));
        updAllResponse.assertThat().body("user." + "email", equalTo(user.getEmail().toLowerCase(Locale.ROOT)));
        updAllResponse.assertThat().body("user." + "name", equalTo(user.getName()));
        userClient.loginUser(user).statusCode(200);
    }
    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void updateEmailWithAuthTest() {
        user.setEmail("test" + user.getEmail());
        ValidatableResponse updEmailResponse = userClient.changeUserData(accessToken.substring(7), user).statusCode(200);
        updEmailResponse.assertThat().body("success", equalTo(true));
        updEmailResponse.assertThat().body("user." + "email", equalTo(user.getEmail().toLowerCase(Locale.ROOT)));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void updatePasswordWithAuthTest() {
        user.setPassword("test" + user.getPassword());
        ValidatableResponse updPasswordResponse = userClient.changeUserData(accessToken.substring(7), user).statusCode(200);
        updPasswordResponse.assertThat().body("success", equalTo(true));
        userClient.loginUser(user).statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void updateNameWithAuthTest() {
        user.setName("test" + user.getName());
        ValidatableResponse updEmailResponse = userClient.changeUserData(accessToken.substring(7), user).statusCode(200);
        updEmailResponse.assertThat().body("success", equalTo(true));
        updEmailResponse.assertThat().body("user." + "name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение всех данных пользователя без авторизации")
    public void updateAllUserDataWithoutAuthTest() {
        user.setEmail("test" + user.getEmail());
        user.setPassword("test" + user.getPassword());
        user.setName("test" + user.getName());
        ValidatableResponse updAllWithoutAuthResponse = userClient.changeUserWithoutToken(user).statusCode(401);
        updAllWithoutAuthResponse.assertThat().body("success", equalTo(false));
        updAllWithoutAuthResponse.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    public void updateEmailWithoutAuthTest() {
        user.setEmail("test" + user.getEmail());
        ValidatableResponse updEmailWithoutAuthResponse = userClient.changeUserWithoutToken(user).statusCode(401);
        updEmailWithoutAuthResponse.assertThat().body("success", equalTo(false));
        updEmailWithoutAuthResponse.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    public void updatePasswordWithoutAuthTest() {
        user.setPassword("test" + user.getPassword());
        ValidatableResponse updPasswordWithoutAuthResponse = userClient.changeUserWithoutToken(user).statusCode(401);
        updPasswordWithoutAuthResponse.assertThat().body("success", equalTo(false));
        updPasswordWithoutAuthResponse.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void updateNameWithoutAuthTest() {
        user.setName("test" + user.getName());
        ValidatableResponse updNameWithoutAuthResponse = userClient.changeUserWithoutToken(user).statusCode(401);
        updNameWithoutAuthResponse.assertThat().body("success", equalTo(false));
        updNameWithoutAuthResponse.assertThat().body("message", equalTo("You should be authorised"));
    }
}
