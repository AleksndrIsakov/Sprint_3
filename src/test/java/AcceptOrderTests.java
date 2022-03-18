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
@Story("2. Принять заказ")
public class AcceptOrderTests {

    private int courierId;
    private int track;
    private int id;

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

        id = RequestsTemplates.getRequest("/api/v1/orders/track?t=" + track)
                .getBody().jsonPath().get("order.id");

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
    @DisplayName("Успешное назначение заказа")
    public void acceptOrder() {
        given()
                .queryParam("courierId", courierId)
                .put(baseURI + "/api/v1/orders/accept/" + id)
                .then()
                .statusCode(200)
                .and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("В заказ не передан id курьера")
    public void checkOrderWithoutCourierId() {
        given()
                .put(baseURI + "/api/v1/orders/accept/" + id)
                .then()
                .statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("В заказ передан неверный id курьера")
    public void checkOrderIncorrectCourierId() {
        given()
                .queryParam("courierId", 0)
                .put(baseURI + "/api/v1/orders/accept/" + id)
                .then()
                .statusCode(404)
                .and().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Не передан номер заказа")
    public void checkOrderWithoutNumber() {
        given()
                .queryParam("courierId", courierId)
                .put(baseURI + "/api/v1/orders/accept/")
                .then()
                .statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }


}
