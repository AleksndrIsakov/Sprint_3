import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

// Создание заказа
@Epic("Основное задание")
@Story("3. Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;
    private OrderClient orderClient;
    private int track;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        orderClient.cancelOrder(track);
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GRAY")},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        Allure.addAttachment("Выбранный цвет:", color.toString());
        Order order = OrderGenerator.customOrder(color);

        ValidatableResponse createResponse = orderClient.createOrder(order);
        int statusCode = createResponse.extract().statusCode();
        track = createResponse.extract().jsonPath().get("track");

        assertThat("отличается код ответа", statusCode, equalTo(SC_CREATED));
        assertThat("ошибка представления трек номера", track, isA(int.class));
    }


}
