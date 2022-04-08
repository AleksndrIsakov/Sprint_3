import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Основное задание")
@Story("1. Создание курьера")
public class CourierCreateTest {

    CourierCredentials courierCredentials;
    CourierClient courierClient;
    Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void tearDown(){
        if (courierCredentials != null) {
            ValidatableResponse loginResponse = courierClient.login(courierCredentials);
            int courierId = loginResponse.extract().path("id");
            if (courierId != 0) courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Проверка создания курьера")
    public void courierCanCreate() {
        ValidatableResponse createResponse = courierClient.create(courier);
        courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        int statusCode = createResponse.extract().statusCode();
        boolean ok = createResponse.extract().jsonPath().get("ok");

        assertThat("отличается код ответа", statusCode, equalTo(SC_CREATED));
        assertThat("отличается сообщение в ответе", ok, equalTo(true));
    }

    @Ignore
    @Test
    @DisplayName("Проверка запрета на создание повторной записи")
    public void courierCantCreateDuplicate() {
        // Создадим первого курьера и сохраним его данные (для удаления в After)
        ValidatableResponse createResponse = courierClient.create(courier);
        courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());

        // Пытаемся создать второго курьера
        createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_CONFLICT));
        assertThat("отличается сообщение в ответе", message, equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с пустым login")
    public void courierCantCreateWithoutLogin() {

        courier.setLogin("");
        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с пустым password")
    public void courierCantCreateWithoutPassword() {

        courier.setPassword("");
        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Ignore
    @Test
    @DisplayName("Проверка обработки запроса с пустым firstName")
    public void courierCantCreateWithoutFirstName() {

        courier.setFirstName("");
        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для создания учетной записи"));
    }
}
