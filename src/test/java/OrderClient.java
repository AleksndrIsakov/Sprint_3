import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Data;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {

    private static final String ORDER_PATH = "/api/v1/orders/";
    private static final String ORDER_ACCEPT_PATH = "/api/v1/orders/accept/";
    private static final String ORDER_BY_NUMBER_PATH = "/api/v1/orders/track/";
    private static final String CANCEL_PATH = "/api/v1/orders/cancel/";

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение заказа по номеру: {track}")
    public ValidatableResponse getOrderByNumber(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParam("t", (track < 0)?"":track)
                .when()
                .get(ORDER_BY_NUMBER_PATH)
                .then();
    }

    @Step("Принять заказ с номером: {id}, на курьера с id: {courierId}")
    public ValidatableResponse acceptOrder(int id, int courierId) {
        return given()
                .spec(getBaseSpec())
                .queryParam("courierId", (courierId < 0)?"":courierId)
                .when()
                .put(ORDER_ACCEPT_PATH + ((id < 0)?"":id))
                .then();
    }

    @Step("Создание заказа {order}")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Отмена заказа по трек номеру: {track}")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParam("track", (track < 0)?"":track)
                .when()
                .put(CANCEL_PATH)
                .then();
    }
}
