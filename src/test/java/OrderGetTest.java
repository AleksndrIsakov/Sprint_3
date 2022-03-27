import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.JSONOrder;

import java.util.Arrays;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Дополнительное задание")
@Story("3. Получить заказ по его номеру")
public class OrderGetTest {

    private OrderClient orderClient;
    private int track;


    @Before
    // Подготовим данные для тестов
    public void setUp() {
        // Создаем заказ, получаем track номер
        orderClient = new OrderClient();
        Order order = OrderGenerator.customOrder(Arrays.asList("BLACK"));
        ValidatableResponse orderCreateResponse = orderClient.createOrder(order);
        track = orderCreateResponse.extract().body().jsonPath().get("track");
    }

    @After
    // Удалим созданные данные
    public void tearDown() {

    }

    @Test
    @DisplayName("Получить заказ по его номеру")
    public void checkGetOrderObject() {

        ValidatableResponse orderResponse = orderClient.getOrderByNumber(track);
        int statusCode = orderResponse.extract().statusCode();
        JSONOrder order = orderResponse.extract().as(JSONOrder.class);

        assertThat("отличается код ответа", statusCode, equalTo(SC_OK));
        assertThat("в ответе нет заказа", order, notNullValue());
    }

    @Test
    @DisplayName("запрос без номера заказа возвращает ошибку")
    public void checkGetOrderWithoutOrderNumber() {

        ValidatableResponse orderResponse = orderClient.getOrderByNumber(-1);
        int statusCode = orderResponse.extract().statusCode();
        String message = orderResponse.extract().path("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    public void checkGetOrderWithNotExistOrderNumber() {

        ValidatableResponse orderResponse = orderClient.getOrderByNumber(0);
        int statusCode = orderResponse.extract().statusCode();
        String message = orderResponse.extract().path("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("отличается сообщение в ответе", message, equalTo("Заказ не найден"));
    }
}
