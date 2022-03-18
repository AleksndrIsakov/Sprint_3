import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.JSONOrder;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

@Epic("Дополнительное задание")
@Story("3. Получить заказ по его номеру")
public class GetOrderTests {

    private int courierId;
    private int track;

    @Before
    // Подготовим данные для тестов
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
        ArrayList<String> courier = ScooterCourier.registerNewCourierAndReturnLoginPassword();
        try {
            courierId = ScooterCourier.getId(courier.get(0), courier.get(1));
        } catch (NullPointerException e) {
            courierId = 0;
        }

        ScooterOrder order = new ScooterOrder();
        track = RequestsTemplates.postRequest("/api/v1/orders", order.getJsonOrderBody())
                .getBody().jsonPath().get("track");

    }

    @After
    // Удалим созданные данные
    public void tearDown() {
        // отменим созданный заказ
        RequestsTemplates.putRequest("/api/v1/orders/cancel", "{\"track\":" + track + "}");
        // удалим пользователя
        if (courierId != 0)
            RequestsTemplates.deleteRequest("/api/v1/courier/" + courierId);
    }

    @Test
    @DisplayName("Получить заказ по его номеру")
    public void checkGetOrderObject() {
        JSONOrder order = RequestsTemplates.getRequest("/api/v1/orders/track?t=" + track)
                .body().as(JSONOrder.class);

        assertNotNull(order);
    }

    @Test
    @DisplayName("запрос без номера заказа возвращает ошибку")
    public void checkGetOrderWithoutOrderNumber() {
        RequestsTemplates.getRequest("/api/v1/orders/track?t=")
                .then()
                .statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    public void checkGetOrderWithNotExistOrderNumber() {
        RequestsTemplates.getRequest("/api/v1/orders/track?t=0")
                .then()
                .statusCode(404)
                .and().body("message", equalTo("Заказ не найден"));
    }
}
