import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierWithAllRequiredFieldsTest() {
        Courier courier = new Courier("elmira", "1234", "segedina");

        Response creationResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        creationResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        System.out.println(creationResponse.body().asString());

        CourierLogin courierLogin = new CourierLogin("elmira", "1234");

        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");

        loginResponse.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(200);

        String IdString = loginResponse.body().asString();
        Gson gson = new Gson();
        CourierDelete id = gson.fromJson(IdString, CourierDelete.class);

        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format("/api/v1/courier/%s", id.getId()));

        deleteResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createTwoIdenticalCouriersTest() {
        Courier firstCourier = new Courier("elmira", "1234", "segedina");

        Response firstCourierCreationResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(firstCourier)
                        .when()
                        .post("/api/v1/courier");

        firstCourierCreationResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        Courier secondCourier = new Courier("elmira", "1234", "segedina");

        Response secondCourierCreationResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(secondCourier)
                        .when()
                        .post("/api/v1/courier");

        secondCourierCreationResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);

        System.out.println(secondCourierCreationResponse.body().asString());

        CourierLogin courierLogin = new CourierLogin("elmira", "1234");

        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");

        loginResponse.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(200);

        String IdString = loginResponse.body().asString();
        Gson gson = new Gson();
        CourierDelete id = gson.fromJson(IdString, CourierDelete.class);

        Response deleteResponse = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format("/api/v1/courier/%s", id.getId()));

        deleteResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createCourierWithoutLoginTest() {
        CourierWithoutLogin courier = new CourierWithoutLogin("1234", "segedina");

        Response creationResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        creationResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        System.out.println(creationResponse.body().asString());
    }

    @Test
    public void createCourierWithoutPasswordTest() {
        CourierWithoutPassword courier = new CourierWithoutPassword("elmira", "segedina");

        Response creationResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        creationResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        System.out.println(creationResponse.body().asString());
    }
}
