import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {

    CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;

    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphabetic(6);
        password = RandomStringUtils.randomAlphabetic(6);

        courierSteps.createCourier(login, password);
    }

    @After
    public void tearDown() {
        Response loginResponse = courierSteps.loginCourier(login, password).extract().response();

        if (loginResponse.getStatusCode() == SC_OK) {
            Integer id = loginResponse.path("id");
            courierSteps.deleteCourier(id);
        }
    }

    @DisplayName("Успешная авторизация курьера при вводе валидного логина и пароля")
    @Description("Успешная авторизация курьера при вводе валидного логина и пароля - возвращает 200 OK")
    @Test
    public void loginWithAllRequiredFieldsTest() {
        courierSteps
                .loginCourier(login, password)
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @DisplayName("Ошибка в авторизации курьера при вводе неверного логина")
    @Description("Ошибка в авторизации курьера при вводе неверного логина - возвращает 404 Not Found")
    @Test
    public void loginWithInvalidLoginTest() {
        String invalidLogin = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .loginCourier(invalidLogin, password)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Ошибка в авторизации курьера при вводе неверного пароля")
    @Description("Ошибка в авторизации курьера при вводе неверного пароля - возвращает 404 Not Found")
    @Test
    public void loginWithInvalidPasswordTest() {
        String invalidPassword = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .loginCourier(login, invalidPassword)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Ошибка в авторизации курьера без ввода логина")
    @Description("Ошибка в авторизации курьера без ввода логина - возвращает 400 Bad Request")
    @Test
    public void loginWithoutLoginFieldTest() {
        courierSteps
                .loginCourier(null, password)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @DisplayName("Ошибка в авторизации курьера без ввода пароля")
    @Description("Ошибка в авторизации курьера без ввода пароля - возвращает 400 Bad Request")
    @Issue("ES15-66")
    @Test
    public void courierLoginWithoutPasswordFieldTest() {
        courierSteps
                .loginCourier(login, null)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
