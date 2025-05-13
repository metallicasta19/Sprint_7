import io.qameta.allure.junit4.DisplayName;
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

        courierSteps.createCourier(login, password, null);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password).extract().path("id");
        courierSteps.deleteCourier(id);
    }

    @DisplayName("Успешная авторизация курьера при вводе валидного логина и пароля")
    @Test
    public void loginWithAllRequiredFieldsTest() {
        courierSteps
                .loginCourier(login, password)
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @DisplayName("Ошибка в авторизации курьера при вводе неверного логина")
    @Test
    public void loginWithInvalidLoginTest() {
        String invalidLogin = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .loginCourier(invalidLogin, password)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Ошибка в авторизации курьера при вводе неверного пароля")
    @Test
    public void loginWithInvalidPasswordTest() {
        String invalidPassword = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .loginCourier(login, invalidPassword)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Ошибка в авторизации курьера без ввода логина")
    @Test
    public void loginWithoutLoginFieldTest() {
        courierSteps
                .loginCourier(null, password)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @DisplayName("Ошибка в авторизации курьера без ввода пароля")
    @Test
    public void courierLoginWithoutPasswordFieldTest() {
        courierSteps
                .loginCourier(login, null)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
