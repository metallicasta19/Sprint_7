import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

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
    }

    @After
    public void tearDown() {
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
    public void loginWithAllRequiredFieldsTest() {
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

        System.out.println(loginResponse.body().asString());
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        CourierLogin courierLogin = new CourierLogin("elmira", "4321");

        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");

        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        System.out.println(loginResponse.body().asString());
    }

    @Test
    public void loginWithoutLoginFieldTest() {
        CourierLoginWithoutLogin courierLogin = new CourierLoginWithoutLogin("1234");

        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");

        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

        System.out.println(loginResponse.body().asString());
    }

//    @Test
//    public void courierLoginWithoutPasswordFieldTest() {
//        CourierLoginWithoutPassword courierLogin = new CourierLoginWithoutPassword("elmira");
//
//        Response loginResponse =
//                given()
//                        .header("Content-type", "application/json")
//                        .and()
//                        .body(courierLogin)
//                        .when()
//                        .post("/api/v1/courier/login");
//
//        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
//                .and()
//                .statusCode(400);
//
//        System.out.println(loginResponse.body().asString());
//    }
}
