import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.isA;

// Создание заказа
@Epic("Основное задание")
@Story("3. Создание заказа")
@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String color;
    private final String apiUrl = "/api/v1/orders";

    public CreateOrderTests(String color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
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
    @DisplayName("Создание заказа:")
    public void createOrder() {
        ScooterOrder order = new ScooterOrder();
        order.setColor(color);

        RequestsTemplates.postRequest(apiUrl, order.getJsonOrderBody())
                .then().statusCode(201)
                .and()
                .body("track", isA(int.class));
    }


}
