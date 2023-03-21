package user;

import client.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UserClient extends Client {

    private static String CREATE_PATH = "/api/auth/register";
    private static String LOGIN_PATH = "/api/auth/login";
    private static String DELETE_PATH = "/api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(User user, String accessToken) {
        given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(DELETE_PATH)
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED);
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Изменение данных пользователя с отправкой токена")
    public ValidatableResponse changeUserData(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(DELETE_PATH)
                .then();
    }

    @Step("Изменение данных пользователя без отправки токена")
    public ValidatableResponse changeUserWithoutToken(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(DELETE_PATH)
                .then();
    }
}