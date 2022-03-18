import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Дополнительное задание")
@Story("3. Получить заказ по его номеру")
public class GetOrderTests {

    private int courierId;
    private int track;

    @Before
    // Подготовим данные для тестов
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        try {
            courierId = ScooterLoginCourier.getId(courier.get(0), courier.get(1));
        } catch (NullPointerException e) {
            courierId = 0;
        }

        String jsonBody = "{\n" +
                "    \"firstName\": \"MyOrder\",\n" +
                "    \"lastName\": \"Uchiha\",\n" +
                "    \"address\": \"Konoha, 142 apt.\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 35\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2020-06-06\",\n" +
                "    \"comment\": \"Saske, come back to Konoha\",\n" +
                "    \"color\": [\n" +
                "        \"BLACK\"\n" +
                "    ]\n" +
                "}";

        track = RequestsTemplates.postRequest("/api/v1/orders", jsonBody)
                .getBody().jsonPath().get("track");

    }

    @Test
    @DisplayName("Получить заказ по его номеру")
    public void getOrder() {
        RequestsTemplates.getRequest("/api/v1/orders/track?t=" + track)
                .then()
                .statusCode(200);
        // TODO: Добавить проверку объекта
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

    @After
    // Удалим созданные данные
    public void tearDown(){
        // отменим созданный заказ
        RequestsTemplates.putRequest("/api/v1/orders/cancel", "{\"track\":" + track + "}");
        // удалим пользователя
        if (courierId != 0)
            RequestsTemplates.deleteRequest("/api/v1/courier/" + courierId, "{}");
    }

}
