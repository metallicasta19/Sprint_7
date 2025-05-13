package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;

import static io.restassured.RestAssured.given;
import static org.example.utils.Endpoints.*;

public class CourierSteps {
    @Step("Создание курьера")
    public ValidatableResponse createCourier(String login, String password, String firstname) {
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        courier.setFirstName(firstname);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(CREATE_COURIER_ENDPOINT)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse loginCourier(String login, String password) {
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(LOGIN_COURIER_ENDPOINT)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .header("Content-type", "application/json")
                .pathParam("id", id)
                .when()
                .delete(DELETE_COURIER_ENDPOINT + "{id}")
                .then();
    }
}
