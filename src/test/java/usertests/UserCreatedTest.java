package usertests;

import io.restassured.response.ValidatableResponse;
import user.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreatedTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.getUser();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if(accessToken != null) {
            userClient.deleteUser(user, accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    public void  successCreatingUserTest(){
        ValidatableResponse createResponse = userClient.createUser(user).statusCode(200);
        createResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание существующего пользователя")
    public void  creatingIdenticalUserTest() {
        ValidatableResponse createUserResponse = userClient.createUser(user).statusCode(200);
        ValidatableResponse createSameUserResponse = userClient.createUser(user).statusCode(403);
        createSameUserResponse.assertThat().body("success", equalTo(false));
        createSameUserResponse.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailTest(){
        user.setEmail("");
        ValidatableResponse createUserWithoutEmailResponse = userClient.createUser(user).statusCode(403);
        createUserWithoutEmailResponse.assertThat().body("success", equalTo(false));
        createUserWithoutEmailResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest(){
        user.setPassword("");
        ValidatableResponse createUserWithoutPasswordResponse = userClient.createUser(user).statusCode(403);
        createUserWithoutPasswordResponse.assertThat().body("success", equalTo(false));
        createUserWithoutPasswordResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse createUserWithoutNameResponse = userClient.createUser(user).statusCode(403);
        createUserWithoutNameResponse.assertThat().body("success", equalTo(false));
        createUserWithoutNameResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
