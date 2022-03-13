import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Список заказов
public class OrderListTests {

    private ArrayList<String> courier;

    @Before
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private Response createRequest(String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .get("/api/v1/orders");
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void checkOrderList() {
        createRequest("{}")
                .then().statusCode(200)
                .and().body("orders", notNullValue());
    }

}
