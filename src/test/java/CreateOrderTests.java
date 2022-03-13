import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String color;

    public CreateOrderTests(String color) {
        this.color = color;
    }

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
                .post("/api/v1/orders");
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"\"BLACK\""},
                {"\"GREY\""},
                {"\"BLACK\",\"GREY\""},
                {"\"\""}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        String jsonBody = "{\n" +
                "    \"firstName\": \"Naruto\",\n" +
                "    \"lastName\": \"Uchiha\",\n" +
                "    \"address\": \"Konoha, 142 apt.\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 35\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2020-06-06\",\n" +
                "    \"comment\": \"Saske, come back to Konoha\",\n" +
                "    \"color\": [\n" +
                "        " + color + "\n" +
                "    ]\n" +
                "}";

        createRequest(jsonBody)
                .then().statusCode(201)
                .and()
                .body("track", isA(int.class));
    }


}
