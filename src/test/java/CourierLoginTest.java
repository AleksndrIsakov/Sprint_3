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
import static org.hamcrest.Matchers.*;

@Epic("Основное задание")
@Story("2. Логин курьера")
public class CourierLoginTest {

    CourierClient courierClient;
    Courier courier;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
    }

    @After
    public void tearDown(){
        if (courierId == 0) {
            ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
            courierId = loginResponse.extract().path("id");
        }
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Проверка авторизации курьера")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat("courier cannot login", statusCode, equalTo(SC_OK));
        assertThat("ошибка в id курьера", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Проверка авторизации курьера без логина")
    public void courierCantLoginWithoutLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("", courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера без пароля")
    public void courierCantLoginWithoutPassword() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), ""));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("отличается сообщение в ответе", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе c некорректным логином")
    public void courierCantLoginWithIncorrectLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getFirstName(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("отличается сообщение в ответе", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе c некорректным паролем")
    public void courierCantLoginWithIncorrectPassword() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getFirstName()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().jsonPath().get("message");

        assertThat("отличается код ответа", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("отличается сообщение в ответе", message, equalTo("Учетная запись не найдена"));
    }

}
