import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.example.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest extends BaseTest {

    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;

    @After
    public void tearDown() {
        Response loginResponse = courierSteps.loginCourier(login, password).extract().response();

        if (loginResponse.getStatusCode() == SC_OK) {
            Integer id = loginResponse.path("id");
            courierSteps.deleteCourier(id);
        }
    }

    @DisplayName("Успешное создание курьера при вводе валидных значений")
    @Description("Успешное создание курьера при вводе валидных значений - возвращает 201 Created")
    @Test
    public void createCourierWithAllRequiredFieldsTest() {
        login = RandomStringUtils.randomAlphabetic(6);
        password = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, password)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @DisplayName("Ошибка при создании двух одинаковых курьеров")
    @Description("Ошибка при создании двух одинаковых курьеров - возвращает 409 Сonflict")
    @Test
    public void createTwoIdenticalCouriersTest() {
        login = RandomStringUtils.randomAlphabetic(6);
        password = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, password)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        courierSteps
                .createCourier(login, password)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Ошибка при создании курьера без логина - возвращает 400 Bad Request")
    @Test
    public void createCourierWithoutLoginFieldTest() {
        password = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(null, password)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Ошибка при создании курьера без пароля - возвращает 400 Bad Request")
    @Test
    public void createCourierWithoutPasswordFieldTest() {
        login = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, null)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
