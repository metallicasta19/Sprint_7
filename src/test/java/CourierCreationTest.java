import io.qameta.allure.junit4.DisplayName;
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
    private String firstname;

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password).extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }

    @DisplayName("Успешное создание курьера при вводе валидных значений")
    @Test
    public void createCourierWithAllRequiredFieldsTest() {
        login = RandomStringUtils.randomAlphabetic(6);
        password = RandomStringUtils.randomAlphabetic(6);
        firstname = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, password, firstname)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @DisplayName("Ошибка при создании двух одинаковых курьеров")
    @Test
    public void createTwoIdenticalCouriersTest() {
        login = RandomStringUtils.randomAlphabetic(6);
        password = RandomStringUtils.randomAlphabetic(6);
        firstname = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, password, firstname)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        courierSteps
                .createCourier(login, password, firstname)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @DisplayName("Ошибка при создании курьера без логина")
    @Test
    public void createCourierWithoutLoginFieldTest() {
        password = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(null, password, null)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @DisplayName("Ошибка при создании курьера без пароля")
    @Test
    public void createCourierWithoutPasswordFieldTest() {
        login = RandomStringUtils.randomAlphabetic(6);

        courierSteps
                .createCourier(login, null, null)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
