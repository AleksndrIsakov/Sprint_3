import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Основное задание")
@Story("4. Список заказов")
public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void checkOrderList() {

        ValidatableResponse getOrdersResponse = orderClient.getOrders();
        int statusCode = getOrdersResponse.extract().statusCode();
        List<String> orders = getOrdersResponse.extract().jsonPath().get("orders");

        assertThat("отличается код ответа", statusCode, equalTo(SC_OK));
        assertThat("список заказов пуст", orders, notNullValue());
    }

}
