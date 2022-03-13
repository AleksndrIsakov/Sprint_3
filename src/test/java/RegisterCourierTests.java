import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterCourierTests {

    private String courierLogin;
    private String courierPassword;
    private String courierFirstName;

    @Before
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        this.courierLogin = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        this.courierPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        this.courierFirstName = RandomStringUtils.randomAlphabetic(10);
    }

    private String courierRegisterJsonBody(String courierLogin, String courierPassword, String courierFirstName) {
        return "{\"login\":\"" + courierLogin + "\","
                + "\"password\":\"" + courierPassword + "\","
                + "\"firstName\":\"" + courierFirstName + "\"}";
    }

    private Response createRequest(String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Проверка создания курьера")
    public void createCourier() {
        String jsonBody = courierRegisterJsonBody(this.courierLogin, this.courierPassword, this.courierFirstName);
        createRequest(jsonBody)
                .then().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка запрета на создание повторной записи")
    public void createDuplicateCourier() {
        String jsonBody = courierRegisterJsonBody(this.courierLogin, this.courierPassword, this.courierFirstName);
        createRequest(jsonBody).then().statusCode(201);

        createRequest(jsonBody)
                .then().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с пустым login")
    public void createCourierWithEmptyLogin() {
        String jsonBody = courierRegisterJsonBody("", this.courierPassword, this.courierFirstName);

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с пустым password")
    public void createCourierWithEmptyPassword() {
        String jsonBody = courierRegisterJsonBody(this.courierLogin, "", this.courierFirstName);

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с пустым firstName")
    public void createCourierWithEmptyFirstName() {
        String jsonBody = courierRegisterJsonBody(this.courierLogin, this.courierPassword, "");

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с отсутствующим login")
    public void createCourierWithoutLogin() {
        String jsonBody = "{\"password\":\"" + courierPassword + "\","
                        + "\"firstName\":\"" + courierFirstName + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с отсутствующим password")
    public void createCourierWithoutPassword() {
        String jsonBody = "{\"login\":\"" + courierLogin + "\","
                         + "\"firstName\":\"" + courierFirstName + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка обработки запроса с отсутствующим firstName")
    public void createCourierWithoutFirstname() {
        String jsonBody = "{\"login\":\"" + courierLogin + "\","
                        + "\"password\":\"" + courierPassword + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
