import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Дополнительное задание")
@Story("2. Принять заказ")
public class OrderAcceptTest {

    private CourierClient courierClient;
    private OrderClient orderClient;
    private int courierId;
    private int track;
    private int id;

    @Before
    // Подготовим данные для тестов
    public void setUp() {
        // Создаем курьера
        courierClient = new CourierClient();
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        courierId = loginResponse.extract().body().jsonPath().get("id");

        // Создаем заказ, получаем track номер
        orderClient = new OrderClient();
        Order order = OrderGenerator.customOrder(Arrays.asList("BLACK"));
        ValidatableResponse orderCreateResponse = orderClient.createOrder(order);
        track = orderCreateResponse.extract().body().jsonPath().get("track");

        // Получаем id заказа
        ValidatableResponse orderNumberResponse = orderClient.getOrderByNumber(track);
        id = orderNumberResponse.extract().body().jsonPath().get("order.id");
    }

    @After
    // Удалим созданные данные
    public void tearDown() {
        orderClient.cancelOrder(track);
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Успешное назначение заказа")
    public void acceptOrder() {
        ValidatableResponse acceptResponse = orderClient.acceptOrder(id,courierId);

        int statusCode = acceptResponse.extract().statusCode();
        boolean ok = acceptResponse.extract().path("ok");

        assertThat("отличается код ответа", statusCode, equalTo(SC_OK));
        assertThat("courier ID is incorrect", ok, equalTo(true));
    }

    @Test
    @DisplayName("В заказ не передан id курьера")
    public void checkOrderWithoutCourierId() {
        ValidatableResponse acceptResponse = orderClient.acceptOrder(id,-1);

        int statusCode = acceptResponse.extract().statusCode();
        String message = acceptResponse.extract().path("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("В заказ передан неверный id курьера")
    public void checkOrderIncorrectCourierId() {
        ValidatableResponse acceptResponse = orderClient.acceptOrder(id,0);

        int statusCode = acceptResponse.extract().statusCode();
        String message = acceptResponse.extract().path("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("отличается сообщение в ответе", message, equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Не передан номер заказа")
    public void checkOrderWithoutNumber() {
        ValidatableResponse acceptResponse = orderClient.acceptOrder(-1,courierId);

        int statusCode = acceptResponse.extract().statusCode();
        String message = acceptResponse.extract().path("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для поиска"));
    }
}
