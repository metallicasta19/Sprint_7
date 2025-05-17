import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    @Before
    public void setupBase() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
