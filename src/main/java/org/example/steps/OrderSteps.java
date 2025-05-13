package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.Orders;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.utils.Endpoints.*;

public class OrderSteps {
    @Step("Создание заказа")
    public Response createOrder(String firstName, String lastName, String address,
                                int metroStation, String phone, int rentTime,
                                String deliveryDate, String comment, List<String> color) {
        Orders order = new Orders();

        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setAddress(address);
        order.setMetroStation(metroStation);
        order.setPhone(phone);
        order.setRentTime(rentTime);
        order.setDeliveryDate(deliveryDate);
        order.setComment(comment);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_ENDPOINT)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .pathParam("track", track)
                .when()
                .delete(CANCEL_ORDER_ENDPOINT + "{track}")
                .then();
    }
}
