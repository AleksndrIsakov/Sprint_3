import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Дополнительное задание")
@Story("1. Удалить курьера")
public class CourierDeleteTest {

    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        courierId = loginResponse.extract().body().jsonPath().get("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourier() {
        ValidatableResponse deleteResponse = courierClient.delete(courierId);

        int statusCode = deleteResponse.extract().statusCode();
        boolean message = deleteResponse.extract().jsonPath().get("ok");

        assertThat("отличается код ответа", statusCode, equalTo(SC_OK));
        assertThat("отличается сообщение в ответе", message, equalTo(true));

        courierId = 0;
    }

    @Test
    @DisplayName("Запрос без id возвращает ошибку")
    public void deleteCourierWithoutId() {
        ValidatableResponse deleteResponse = courierClient.delete(-1);

        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Запрос с несуществующим id возвращает ошибку")
    public void deleteCourierWithNotExistId() {
        ValidatableResponse deleteResponse = courierClient.delete(1);

        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("отличается сообщение в ответе", message, equalTo("Курьера с таким id нет"));
    }
}
