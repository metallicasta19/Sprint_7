import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.OrderSteps;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends BaseTest {

    @DisplayName("Успешное получение списка заказов")
    @Description("Успешное получение списка заказов - возвращает 200 OK")
    @Test
    public void checkOrderListTest() {
        OrderSteps orderSteps = new OrderSteps();

        orderSteps
                .getOrderList()
                .statusCode(SC_OK)
                .body(notNullValue());
    }
}
